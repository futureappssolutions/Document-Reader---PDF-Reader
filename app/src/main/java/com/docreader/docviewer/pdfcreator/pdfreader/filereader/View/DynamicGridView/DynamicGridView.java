package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.core.view.MotionEventCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DynamicGridView extends GridView {
    private final List<Long> idList = new ArrayList<>();
    private final List<ObjectAnimator> mWobbleAnimators = new LinkedList<>();
    private long mMobileItemId = -1;
    private int mOverlapIfSwitchStraightLine;
    private int mScrollState = 0;
    private int mSmoothScrollAmountAtEdge = 0;
    private int mActivePointerId = -1;
    private int mLastEventX = -1;
    private int mLastEventY = -1;
    private int mTotalOffsetX = 0;
    private int mTotalOffsetY = 0;
    private int mDownX = -1;
    private int mDownY = -1;
    private boolean mCellIsMobile = false;
    private boolean mIsEditMode = false;
    private boolean mIsEditModeEnabled = true;
    private boolean mIsWaitingForScrollFinish = false;
    private boolean mWobbleInEditMode = true;
    private boolean mIsMobileScrolling;
    private boolean mHoverAnimation;
    private boolean mUndoSupportEnabled;
    private boolean mReorderAnimation;
    private DynamicGridModification mCurrentModification;
    private OnDragListener mDragListener;
    private OnDropListener mDropListener;
    private OnEditModeChangeListener mEditModeChangeListener;
    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;
    private View mMobileView;
    private Stack<DynamicGridModification> mModificationStack;
    private OnSelectedItemBitmapCreationListener mSelectedItemBitmapCreationListener;
    private AdapterView.OnItemClickListener mUserItemClickListener;
    private AbsListView.OnScrollListener mUserScrollListener;

    private final AdapterView.OnItemClickListener mLocalItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (!isEditMode() && isEnabled() && mUserItemClickListener != null) {
                mUserItemClickListener.onItemClick(adapterView, view, i, j);
            }
        }
    };

    private final AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        private int mCurrentFirstVisibleItem;
        private int mCurrentScrollState;
        private int mCurrentVisibleItemCount;
        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            mCurrentFirstVisibleItem = i;
            mCurrentVisibleItemCount = i2;
            int i4 = mPreviousFirstVisibleItem;
            if (i4 == -1) {
                i4 = i;
            }
            mPreviousFirstVisibleItem = i4;
            int i5 = mPreviousVisibleItemCount;
            if (i5 == -1) {
                i5 = i2;
            }
            mPreviousVisibleItemCount = i5;
            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();
            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
            if (isPostHoneycomb() && mWobbleInEditMode) {
                updateWobbleState(i2);
            }
            if (mUserScrollListener != null) {
                mUserScrollListener.onScroll(absListView, i, i2, i3);
            }
        }

        private void updateWobbleState(int i) {
            for (int i2 = 0; i2 < i; i2++) {
                View childAt = getChildAt(i2);
                if (childAt != null) {
                    if (mMobileItemId != -1 && Boolean.TRUE != childAt.getTag(R.id.dgv_wobble_tag)) {
                        if (i2 % 2 == 0) {
                            animateWobble(childAt);
                        } else {
                            animateWobbleInverse(childAt);
                        }
                        childAt.setTag(R.id.dgv_wobble_tag, true);
                    } else if (mMobileItemId == -1 && childAt.getRotation() != 0.0f) {
                        childAt.setRotation(0.0f);
                        childAt.setTag(R.id.dgv_wobble_tag, false);
                    }
                }
            }
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
            mCurrentScrollState = i;
            mScrollState = i;
            isScrollCompleted();
            if (mUserScrollListener != null) {
                mUserScrollListener.onScrollStateChanged(absListView, i);
            }
        }

        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == 0) {
                if (mCellIsMobile && mIsMobileScrolling) {
                    handleMobileCellScroll();
                } else if (mIsWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem && mCellIsMobile && mMobileItemId != -1) {
                DynamicGridView dynamicGridView = DynamicGridView.this;
                dynamicGridView.updateNeighborViewsForId(dynamicGridView.mMobileItemId);
                handleCellSwitch();
            }
        }

        public void checkAndHandleLastVisibleCellChange() {
            if (mCurrentFirstVisibleItem + mCurrentVisibleItemCount != mPreviousFirstVisibleItem + mPreviousVisibleItemCount && mCellIsMobile && mMobileItemId != -1) {
                DynamicGridView dynamicGridView = DynamicGridView.this;
                dynamicGridView.updateNeighborViewsForId(dynamicGridView.mMobileItemId);
                handleCellSwitch();
            }
        }
    };

    public interface OnDragListener {
        void onDragPositionsChanged(int i, int i2);

        void onDragStarted(int i);
    }

    public interface OnDropListener {
        void onActionDrop();
    }

    public interface OnEditModeChangeListener {
        void onEditModeChanged(boolean z);
    }

    public interface OnSelectedItemBitmapCreationListener {
        void onPostSelectedItemBitmapCreation(View view, int i, long j);

        void onPreSelectedItemBitmapCreation(View view, int i, long j);
    }

    private interface SwitchCellAnimator {
        void animateSwitchCell(int i, int i2);
    }

    static /* synthetic */ void access$412(DynamicGridView dynamicGridView, int i) {
        dynamicGridView.mTotalOffsetY = dynamicGridView.mTotalOffsetY + i;
    }

    static /* synthetic */ void access$512(DynamicGridView dynamicGridView, int i) {
        dynamicGridView.mTotalOffsetX = dynamicGridView.mTotalOffsetX + i;
    }

    public DynamicGridView(Context context) {
        super(context);
        init(context);
    }

    public DynamicGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public DynamicGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        mUserScrollListener = onScrollListener;
    }

    public void setOnDropListener(OnDropListener onDropListener) {
        mDropListener = onDropListener;
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        mDragListener = onDragListener;
    }

    public void startEditMode() {
        startEditMode(-1);
    }

    public void startEditMode(int i) {
        if (mIsEditModeEnabled) {
            requestDisallowInterceptTouchEvent(true);
            if (isPostHoneycomb() && mWobbleInEditMode) {
                startWobbleAnimation();
            }
            if (i != -1) {
                startDragAtPosition(i);
            }
            mIsEditMode = true;
            OnEditModeChangeListener onEditModeChangeListener = mEditModeChangeListener;
            if (onEditModeChangeListener != null) {
                onEditModeChangeListener.onEditModeChanged(true);
            }
        }
    }

    public void stopEditMode() {
        mIsEditMode = false;
        requestDisallowInterceptTouchEvent(false);
        if (isPostHoneycomb() && mWobbleInEditMode) {
            stopWobble(true);
        }
        OnEditModeChangeListener onEditModeChangeListener = mEditModeChangeListener;
        if (onEditModeChangeListener != null) {
            onEditModeChangeListener.onEditModeChanged(false);
        }
    }

    public boolean isEditModeEnabled() {
        return mIsEditModeEnabled;
    }

    public void setEditModeEnabled(boolean z) {
        mIsEditModeEnabled = z;
    }

    public void setOnEditModeChangeListener(OnEditModeChangeListener onEditModeChangeListener) {
        mEditModeChangeListener = onEditModeChangeListener;
    }

    public boolean isEditMode() {
        return mIsEditMode;
    }

    public boolean isWobbleInEditMode() {
        return mWobbleInEditMode;
    }

    public void setWobbleInEditMode(boolean z) {
        mWobbleInEditMode = z;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mUserItemClickListener = onItemClickListener;
        super.setOnItemClickListener(mLocalItemClickListener);
    }

    public boolean isUndoSupportEnabled() {
        return mUndoSupportEnabled;
    }

    public void setUndoSupportEnabled(boolean z) {
        if (mUndoSupportEnabled != z) {
            if (z) {
                mModificationStack = new Stack<>();
            } else {
                mModificationStack = null;
            }
        }
        mUndoSupportEnabled = z;
    }

    public void undoLastModification() {
        Stack<DynamicGridModification> stack;
        if (mUndoSupportEnabled && (stack = mModificationStack) != null && !stack.isEmpty()) {
            undoModification(mModificationStack.pop());
        }
    }

    public void undoAllModifications() {
        Stack<DynamicGridModification> stack;
        if (mUndoSupportEnabled && (stack = mModificationStack) != null && !stack.isEmpty()) {
            while (!mModificationStack.isEmpty()) {
                undoModification(mModificationStack.pop());
            }
        }
    }

    public void clearModificationHistory() {
        mModificationStack.clear();
    }

    public void setOnSelectedItemBitmapCreationListener(OnSelectedItemBitmapCreationListener onSelectedItemBitmapCreationListener) {
        mSelectedItemBitmapCreationListener = onSelectedItemBitmapCreationListener;
    }

    private void undoModification(DynamicGridModification dynamicGridModification) {
        for (Pair next : dynamicGridModification.getTransitions()) {
            reorderElements((Integer) next.second, (Integer) next.first);
        }
    }

    private void startWobbleAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (!(childAt == null || Boolean.TRUE == childAt.getTag(R.id.dgv_wobble_tag))) {
                if (i % 2 == 0) {
                    animateWobble(childAt);
                } else {
                    animateWobbleInverse(childAt);
                }
                childAt.setTag(R.id.dgv_wobble_tag, true);
            }
        }
    }

    private void stopWobble(boolean z) {
        for (ObjectAnimator cancel : mWobbleAnimators) {
            cancel.cancel();
        }
        mWobbleAnimators.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != null) {
                if (z) {
                    childAt.setRotation(0.0f);
                }
                childAt.setTag(R.id.dgv_wobble_tag, false);
            }
        }
    }

    private void restartWobble() {
        stopWobble(false);
        startWobbleAnimation();
    }

    public void init(Context context) {
        super.setOnScrollListener(mScrollListener);
        mSmoothScrollAmountAtEdge = (int) ((context.getResources().getDisplayMetrics().density * 8.0f) + 0.5f);
        mOverlapIfSwitchStraightLine = getResources().getDimensionPixelSize(R.dimen.dgv_overlap_if_switch_straight_line);
    }


    public void animateWobble(View view) {
        ObjectAnimator createBaseWobble = createBaseWobble(view);
        createBaseWobble.setFloatValues(-2.0f, 2.0f);
        createBaseWobble.start();
        mWobbleAnimators.add(createBaseWobble);
    }


    public void animateWobbleInverse(View view) {
        ObjectAnimator createBaseWobble = createBaseWobble(view);
        createBaseWobble.setFloatValues(2.0f, -2.0f);
        createBaseWobble.start();
        mWobbleAnimators.add(createBaseWobble);
    }

    @SuppressLint("WrongConstant")
    private ObjectAnimator createBaseWobble(final View view) {
        if (!isPreLollipop()) {
            view.setLayerType(1, null);
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(180);
        objectAnimator.setRepeatMode(2);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setPropertyName("rotation");
        objectAnimator.setTarget(view);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                view.setLayerType(0, null);
            }
        });
        return objectAnimator;
    }

    private void reorderElements(int i, int i2) {
        OnDragListener onDragListener = mDragListener;
        if (onDragListener != null) {
            onDragListener.onDragPositionsChanged(i, i2);
        }
        getAdapterInterface().reorderItems(i, i2);
    }

    private int getColumnCount() {
        return getAdapterInterface().getColumnCount();
    }

    private DynamicGridAdapterInterface getAdapterInterface() {
        return (DynamicGridAdapterInterface) getAdapter();
    }

    private BitmapDrawable getAndAddHoverView(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int top = view.getTop();
        int left = view.getLeft();
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), getBitmapFromView(view));
        mHoverCellOriginalBounds = new Rect(left, top, width + left, height + top);
        Rect rect = new Rect(mHoverCellOriginalBounds);
        mHoverCellCurrentBounds = rect;
        bitmapDrawable.setBounds(rect);
        return bitmapDrawable;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }


    public void updateNeighborViewsForId(long j) {
        idList.clear();
        int positionForID = getPositionForID(j);
        for (int firstVisiblePosition = getFirstVisiblePosition(); firstVisiblePosition <= getLastVisiblePosition(); firstVisiblePosition++) {
            if (positionForID != firstVisiblePosition && getAdapterInterface().canReorder(firstVisiblePosition)) {
                idList.add(getId(firstVisiblePosition));
            }
        }
    }

    public int getPositionForID(long j) {
        View viewForId = getViewForId(j);
        if (viewForId == null) {
            return -1;
        }
        return getPositionForView(viewForId);
    }

    public View getViewForId(long j) {
        int firstVisiblePosition = getFirstVisiblePosition();
        ListAdapter adapter = getAdapter();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (adapter.getItemId(firstVisiblePosition + i) == j) {
                return childAt;
            }
        }
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnDropListener onDropListener;
        DynamicGridModification dynamicGridModification;
        OnDropListener onDropListener2;
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            mDownX = (int) motionEvent.getX();
            mDownY = (int) motionEvent.getY();
            mActivePointerId = motionEvent.getPointerId(0);
            if (mIsEditMode && isEnabled()) {
                layoutChildren();
                startDragAtPosition(pointToPosition(mDownX, mDownY));
            } else if (!isEnabled()) {
                return false;
            }
        } else if (action == 1) {
            touchEventsEnded();
            if (mUndoSupportEnabled && (dynamicGridModification = mCurrentModification) != null && !dynamicGridModification.getTransitions().isEmpty()) {
                mModificationStack.push(mCurrentModification);
                mCurrentModification = new DynamicGridModification();
            }
            if (!(mHoverCell == null || (onDropListener = mDropListener) == null)) {
                onDropListener.onActionDrop();
            }
        } else if (action == 2) {
            int i = mActivePointerId;
            if (i != -1) {
                int findPointerIndex = motionEvent.findPointerIndex(i);
                mLastEventY = (int) motionEvent.getY(findPointerIndex);
                int x = (int) motionEvent.getX(findPointerIndex);
                mLastEventX = x;
                int i2 = mLastEventY - mDownY;
                int i3 = x - mDownX;
                if (mCellIsMobile) {
                    mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left + i3 + mTotalOffsetX, mHoverCellOriginalBounds.top + i2 + mTotalOffsetY);
                    mHoverCell.setBounds(mHoverCellCurrentBounds);
                    invalidate();
                    handleCellSwitch();
                    mIsMobileScrolling = false;
                    handleMobileCellScroll();
                    return false;
                }
            }
        } else if (action == 3) {
            touchEventsCancelled();
            if (!(mHoverCell == null || (onDropListener2 = mDropListener) == null)) {
                onDropListener2.onActionDrop();
            }
        } else if (action == 6 && motionEvent.getPointerId((motionEvent.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8) == mActivePointerId) {
            touchEventsEnded();
        }
        return super.onTouchEvent(motionEvent);
    }

    private void startDragAtPosition(int i) {
        mTotalOffsetY = 0;
        mTotalOffsetX = 0;
        View childAt = getChildAt(i - getFirstVisiblePosition());
        if (childAt != null) {
            long itemId = getAdapter().getItemId(i);
            mMobileItemId = itemId;
            OnSelectedItemBitmapCreationListener onSelectedItemBitmapCreationListener = mSelectedItemBitmapCreationListener;
            if (onSelectedItemBitmapCreationListener != null) {
                onSelectedItemBitmapCreationListener.onPreSelectedItemBitmapCreation(childAt, i, itemId);
            }
            mHoverCell = getAndAddHoverView(childAt);
            OnSelectedItemBitmapCreationListener onSelectedItemBitmapCreationListener2 = mSelectedItemBitmapCreationListener;
            if (onSelectedItemBitmapCreationListener2 != null) {
                onSelectedItemBitmapCreationListener2.onPostSelectedItemBitmapCreation(childAt, i, mMobileItemId);
            }
            if (isPostHoneycomb()) {
                childAt.setVisibility(View.INVISIBLE);
            }
            mCellIsMobile = true;
            updateNeighborViewsForId(mMobileItemId);
            OnDragListener onDragListener = mDragListener;
            if (onDragListener != null) {
                onDragListener.onDragStarted(i);
            }
        }
    }


    public void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    public boolean handleMobileCellScroll(Rect rect) {
        int computeVerticalScrollOffset = computeVerticalScrollOffset();
        int height = getHeight();
        int computeVerticalScrollExtent = computeVerticalScrollExtent();
        int computeVerticalScrollRange = computeVerticalScrollRange();
        int i = rect.top;
        int height2 = rect.height();
        if (i <= 0 && computeVerticalScrollOffset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0);
            return true;
        } else if (i + height2 < height || computeVerticalScrollOffset + computeVerticalScrollExtent >= computeVerticalScrollRange) {
            return false;
        } else {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0);
            return true;
        }
    }

    public void setAdapter(ListAdapter listAdapter) {
        super.setAdapter(listAdapter);
    }


    public void touchEventsEnded() {
        View viewForId = getViewForId(mMobileItemId);
        if (viewForId == null || (!mCellIsMobile && !mIsWaitingForScrollFinish)) {
            touchEventsCancelled();
            return;
        }
        mCellIsMobile = false;
        mIsWaitingForScrollFinish = false;
        mIsMobileScrolling = false;
        mActivePointerId = -1;
        if (mScrollState != 0) {
            mIsWaitingForScrollFinish = true;
            return;
        }
        mHoverCellCurrentBounds.offsetTo(viewForId.getLeft(), viewForId.getTop());
        if (Build.VERSION.SDK_INT > 11) {
            animateBounds(viewForId);
            return;
        }
        mHoverCell.setBounds(mHoverCellCurrentBounds);
        invalidate();
        reset(viewForId);
    }

    private void animateBounds(final View view) {
        TypeEvaluator<Rect> r0 = new TypeEvaluator<Rect>() {
            public int interpolate(int i, int i2, float f) {
                return (int) (((float) i) + (f * ((float) (i2 - i))));
            }

            public Rect evaluate(float f, Rect rect, Rect rect2) {
                return new Rect(interpolate(rect.left, rect2.left, f), interpolate(rect.top, rect2.top, f), interpolate(rect.right, rect2.right, f), interpolate(rect.bottom, rect2.bottom, f));
            }
        };
        ObjectAnimator ofObject = ObjectAnimator.ofObject(mHoverCell, "bounds", r0, mHoverCellCurrentBounds);
        ofObject.addUpdateListener(valueAnimator -> invalidate());
        ofObject.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                mHoverAnimation = true;
                updateEnableState();
            }

            public void onAnimationEnd(Animator animator) {
                mHoverAnimation = false;
                updateEnableState();
                reset(view);
            }
        });
        ofObject.start();
    }


    public void reset(View view) {
        idList.clear();
        mMobileItemId = -1;
        view.setVisibility(View.VISIBLE);
        mHoverCell = null;
        if (isPostHoneycomb() && mWobbleInEditMode) {
            if (mIsEditMode) {
                restartWobble();
            } else {
                stopWobble(true);
            }
        }
        for (int i = 0; i < getLastVisiblePosition() - getFirstVisiblePosition(); i++) {
            View childAt = getChildAt(i);
            if (childAt != null) {
                childAt.setVisibility(View.VISIBLE);
            }
        }
        invalidate();
    }


    public void updateEnableState() {
        setEnabled(!mHoverAnimation && !mReorderAnimation);
    }


    public boolean isPostHoneycomb() {
        return true;
    }

    public static boolean isPreLollipop() {
        return false;
    }

    private void touchEventsCancelled() {
        View viewForId = getViewForId(mMobileItemId);
        if (mCellIsMobile) {
            reset(viewForId);
        }
        mCellIsMobile = false;
        mIsMobileScrolling = false;
        mActivePointerId = -1;
    }


    public void handleCellSwitch() {
        SwitchCellAnimator switchCellAnimator;
        int i = mLastEventY - mDownY;
        int i2 = mLastEventX - mDownX;
        int centerY = mHoverCellOriginalBounds.centerY() + mTotalOffsetY + i;
        int centerX = mHoverCellOriginalBounds.centerX() + mTotalOffsetX + i2;
        View viewForId = getViewForId(mMobileItemId);
        mMobileView = viewForId;
        Point columnAndRowForView = getColumnAndRowForView(viewForId);
        float f = 0.0f;
        View view = null;
        float f2 = 0.0f;
        for (Long longValue : idList) {
            View viewForId2 = getViewForId(longValue);
            if (viewForId2 != null) {
                Point columnAndRowForView2 = getColumnAndRowForView(viewForId2);
                if ((aboveRight(columnAndRowForView2, columnAndRowForView) && centerY < viewForId2.getBottom() && centerX > viewForId2.getLeft()) || ((aboveLeft(columnAndRowForView2, columnAndRowForView) && centerY < viewForId2.getBottom() && centerX < viewForId2.getRight()) || ((belowRight(columnAndRowForView2, columnAndRowForView) && centerY > viewForId2.getTop() && centerX > viewForId2.getLeft()) || ((belowLeft(columnAndRowForView2, columnAndRowForView) && centerY > viewForId2.getTop() && centerX < viewForId2.getRight()) || ((above(columnAndRowForView2, columnAndRowForView) && centerY < viewForId2.getBottom() - mOverlapIfSwitchStraightLine) || ((below(columnAndRowForView2, columnAndRowForView) && centerY > viewForId2.getTop() + mOverlapIfSwitchStraightLine) || ((right(columnAndRowForView2, columnAndRowForView) && centerX > viewForId2.getLeft() + mOverlapIfSwitchStraightLine) || (left(columnAndRowForView2, columnAndRowForView) && centerX < viewForId2.getRight() - mOverlapIfSwitchStraightLine)))))))) {
                    float abs = Math.abs(DynamicGridUtils.getViewX(viewForId2) - DynamicGridUtils.getViewX(mMobileView));
                    float abs2 = Math.abs(DynamicGridUtils.getViewY(viewForId2) - DynamicGridUtils.getViewY(mMobileView));
                    if (abs >= f && abs2 >= f2) {
                        view = viewForId2;
                        f = abs;
                        f2 = abs2;
                    }
                }
            }
        }
        if (view != null) {
            int positionForView = getPositionForView(mMobileView);
            int positionForView2 = getPositionForView(view);
            DynamicGridAdapterInterface adapterInterface = getAdapterInterface();
            if (positionForView2 == -1 || !adapterInterface.canReorder(positionForView) || !adapterInterface.canReorder(positionForView2)) {
                updateNeighborViewsForId(mMobileItemId);
                return;
            }
            reorderElements(positionForView, positionForView2);
            if (mUndoSupportEnabled) {
                mCurrentModification.addTransition(positionForView, positionForView2);
            }
            mDownY = mLastEventY;
            mDownX = mLastEventX;
            if (isPostHoneycomb() && isPreLollipop()) {
                switchCellAnimator = new KitKatSwitchCellAnimator(i2, i);
            } else if (isPreLollipop()) {
                switchCellAnimator = new PreHoneycombCellAnimator(i2, i);
            } else {
                switchCellAnimator = new LSwitchCellAnimator(i2, i);
            }
            updateNeighborViewsForId(mMobileItemId);
            switchCellAnimator.animateSwitchCell(positionForView, positionForView2);
        }
    }

    private class PreHoneycombCellAnimator implements SwitchCellAnimator {
        private final int mDeltaX;
        private final int mDeltaY;

        public PreHoneycombCellAnimator(int i, int i2) {
            mDeltaX = i;
            mDeltaY = i2;
        }

        public void animateSwitchCell(int i, int i2) {
            DynamicGridView.access$412(DynamicGridView.this, mDeltaY);
            DynamicGridView.access$512(DynamicGridView.this, mDeltaX);
        }
    }

    private class KitKatSwitchCellAnimator implements SwitchCellAnimator {
        public int mDeltaX;
        public int mDeltaY;

        public KitKatSwitchCellAnimator(int i, int i2) {
            mDeltaX = i;
            mDeltaY = i2;
        }

        public void animateSwitchCell(int i, int i2) {
            getViewTreeObserver().addOnPreDrawListener(new AnimateSwitchViewOnPreDrawListener(mMobileView, i, i2));
            DynamicGridView dynamicGridView = DynamicGridView.this;
            dynamicGridView.mMobileView = dynamicGridView.getViewForId(dynamicGridView.mMobileItemId);
        }

        private class AnimateSwitchViewOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
            private final int mOriginalPosition;
            private final View mPreviousMobileView;
            private final int mTargetPosition;

            AnimateSwitchViewOnPreDrawListener(View view, int i, int i2) {
                mPreviousMobileView = view;
                mOriginalPosition = i;
                mTargetPosition = i2;
            }

            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                DynamicGridView.access$412(DynamicGridView.this, KitKatSwitchCellAnimator.this.mDeltaY);
                DynamicGridView.access$512(DynamicGridView.this, KitKatSwitchCellAnimator.this.mDeltaX);
                animateReorder(mOriginalPosition, mTargetPosition);
                mPreviousMobileView.setVisibility(View.VISIBLE);
                if (mMobileView == null) {
                    return true;
                }
                mMobileView.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }

    private class LSwitchCellAnimator implements SwitchCellAnimator {

        public int mDeltaX;

        public int mDeltaY;

        public LSwitchCellAnimator(int i, int i2) {
            mDeltaX = i;
            mDeltaY = i2;
        }

        public void animateSwitchCell(int i, int i2) {
            getViewTreeObserver().addOnPreDrawListener(new AnimateSwitchViewOnPreDrawListener(i, i2));
        }

        private class AnimateSwitchViewOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
            private final int mOriginalPosition;
            private final int mTargetPosition;

            AnimateSwitchViewOnPreDrawListener(int i, int i2) {
                mOriginalPosition = i;
                mTargetPosition = i2;
            }

            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                DynamicGridView.access$412(DynamicGridView.this, LSwitchCellAnimator.this.mDeltaY);
                DynamicGridView.access$512(DynamicGridView.this, LSwitchCellAnimator.this.mDeltaX);
                animateReorder(mOriginalPosition, mTargetPosition);
                mMobileView.setVisibility(View.VISIBLE);
                mMobileView = getViewForId(mMobileItemId);
                mMobileView.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }

    private boolean belowLeft(Point point, Point point2) {
        return point.y > point2.y && point.x < point2.x;
    }

    private boolean belowRight(Point point, Point point2) {
        return point.y > point2.y && point.x > point2.x;
    }

    private boolean aboveLeft(Point point, Point point2) {
        return point.y < point2.y && point.x < point2.x;
    }

    private boolean aboveRight(Point point, Point point2) {
        return point.y < point2.y && point.x > point2.x;
    }

    private boolean above(Point point, Point point2) {
        return point.y < point2.y && point.x == point2.x;
    }

    private boolean below(Point point, Point point2) {
        return point.y > point2.y && point.x == point2.x;
    }

    private boolean right(Point point, Point point2) {
        return point.y == point2.y && point.x > point2.x;
    }

    private boolean left(Point point, Point point2) {
        return point.y == point2.y && point.x < point2.x;
    }

    private Point getColumnAndRowForView(View view) {
        int positionForView = getPositionForView(view);
        int columnCount = getColumnCount();
        return new Point(positionForView % columnCount, positionForView / columnCount);
    }

    private long getId(int i) {
        return getAdapter().getItemId(i);
    }


    public void animateReorder(int i, int i2) {
        boolean z = i2 > i;
        LinkedList<Animator> linkedList = new LinkedList<>();
        if (z) {
            int min = i;
            while (min < i2) {
                View viewForId = getViewForId(getId(min));
                min++;
                if (min % getColumnCount() == 0) {
                    linkedList.add(createTranslationAnimations(viewForId, (float) ((-viewForId.getWidth()) * (getColumnCount() - 1)), 0.0f, (float) viewForId.getHeight(), 0.0f));
                } else {
                    linkedList.add(createTranslationAnimations(viewForId, (float) viewForId.getWidth(), 0.0f, 0.0f, 0.0f));
                }
            }
        } else {
            for (int max = i; max > i2; max--) {
                View viewForId2 = getViewForId(getId(max));
                if ((getColumnCount() + max) % getColumnCount() == 0) {
                    linkedList.add(createTranslationAnimations(viewForId2, (float) (viewForId2.getWidth() * (getColumnCount() - 1)), 0.0f, (float) (-viewForId2.getHeight()), 0.0f));
                } else {
                    linkedList.add(createTranslationAnimations(viewForId2, (float) (-viewForId2.getWidth()), 0.0f, 0.0f, 0.0f));
                }
            }
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(linkedList);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                mReorderAnimation = true;
                updateEnableState();
            }

            public void onAnimationEnd(Animator animator) {
                mReorderAnimation = false;
                updateEnableState();
            }
        });
        animatorSet.start();
    }

    private AnimatorSet createTranslationAnimations(View view, float f, float f2, float f3, float f4) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationX", f, f2);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", f3, f4);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }


    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        BitmapDrawable bitmapDrawable = mHoverCell;
        if (bitmapDrawable != null) {
            bitmapDrawable.draw(canvas);
        }
    }

    private static class DynamicGridModification {
        private final List<Pair<Integer, Integer>> transitions = new Stack<>();

        DynamicGridModification() {
        }

        public boolean hasTransitions() {
            return !transitions.isEmpty();
        }

        public void addTransition(int i, int i2) {
            transitions.add(new Pair<>(i, i2));
        }

        public List<Pair<Integer, Integer>> getTransitions() {
            Collections.reverse(transitions);
            return transitions;
        }
    }
}
