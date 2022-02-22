package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class ActionImageView extends AppCompatImageView {
    private boolean activated;
    private int activatedColor;
    private int deactivatedColor;
    private int disabledColor;
    private boolean enabled;
    private int enabledColor;
    private ActionType mActionType;
    private Context mContext;
    private RichEditorAction mRichEditorAction;

    public void resetStatus() {
    }

    public ActionImageView(Context context) {
        this(context, null);
    }

    public ActionImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.enabled = true;
        this.activated = true;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ActionImageView);
        mActionType = ActionType.fromInteger(obtainStyledAttributes.getInteger(0, 0));
        obtainStyledAttributes.recycle();
    }

    public ActionType getActionType() {
        return this.mActionType;
    }

    public void setActionType(ActionType actionType) {
        this.mActionType = actionType;
    }

    public RichEditorAction getRichEditorAction() {
        return this.mRichEditorAction;
    }

    public void setRichEditorAction(RichEditorAction richEditorAction) {
        this.mRichEditorAction = richEditorAction;
    }

    @Override
    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setActivated(boolean z) {
        this.activated = z;
    }

    @Override
    public boolean isActivated() {
        return this.activated;
    }

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$solotech$utilities$mricheditor$ActionType;

        static {
            int[] iArr = new int[ActionType.values().length];
            $SwitchMap$com$solotech$utilities$mricheditor$ActionType = iArr;
            try {
                iArr[ActionType.BOLD.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.ITALIC.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.UNDERLINE.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.SUBSCRIPT.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.SUPERSCRIPT.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.STRIKETHROUGH.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.NORMAL.ordinal()] = 7;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H1.ordinal()] = 8;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H2.ordinal()] = 9;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H3.ordinal()] = 10;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H4.ordinal()] = 11;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H5.ordinal()] = 12;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.H6.ordinal()] = 13;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.JUSTIFY_LEFT.ordinal()] = 14;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.JUSTIFY_CENTER.ordinal()] = 15;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.JUSTIFY_RIGHT.ordinal()] = 16;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.JUSTIFY_FULL.ordinal()] = 17;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.ORDERED.ordinal()] = 18;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.UNORDERED.ordinal()] = 19;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.INDENT.ordinal()] = 20;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.OUTDENT.ordinal()] = 21;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.LINE.ordinal()] = 22;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.BLOCK_QUOTE.ordinal()] = 23;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.BLOCK_CODE.ordinal()] = 24;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.CODE_VIEW.ordinal()] = 25;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.FAMILY.ordinal()] = 26;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.SIZE.ordinal()] = 27;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.LINE_HEIGHT.ordinal()] = 28;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.FORE_COLOR.ordinal()] = 29;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.BACK_COLOR.ordinal()] = 30;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.IMAGE.ordinal()] = 31;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.LINK.ordinal()] = 32;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                $SwitchMap$com$solotech$utilities$mricheditor$ActionType[ActionType.TABLE.ordinal()] = 33;
            } catch (NoSuchFieldError ignored) {
            }
        }
    }

    public void command() {
        switch (AnonymousClass2.$SwitchMap$com$solotech$utilities$mricheditor$ActionType[this.mActionType.ordinal()]) {
            case 1:
                this.mRichEditorAction.bold();
                return;
            case 2:
                this.mRichEditorAction.italic();
                return;
            case 3:
                this.mRichEditorAction.underline();
                return;
            case 4:
                this.mRichEditorAction.subscript();
                return;
            case 5:
                this.mRichEditorAction.superscript();
                return;
            case 6:
                this.mRichEditorAction.strikethrough();
                return;
            case 7:
                this.mRichEditorAction.formatPara();
                return;
            case 8:
                this.mRichEditorAction.formatH1();
                return;
            case 9:
                this.mRichEditorAction.formatH2();
                return;
            case 10:
                this.mRichEditorAction.formatH3();
                return;
            case 11:
                this.mRichEditorAction.formatH4();
                return;
            case 12:
                this.mRichEditorAction.formatH5();
                return;
            case 13:
                this.mRichEditorAction.formatH6();
                return;
            case 14:
                this.mRichEditorAction.justifyLeft();
                return;
            case 15:
                this.mRichEditorAction.justifyCenter();
                return;
            case 16:
                this.mRichEditorAction.justifyRight();
                return;
            case 17:
                this.mRichEditorAction.justifyFull();
                return;
            case 18:
                this.mRichEditorAction.insertOrderedList();
                return;
            case 19:
                this.mRichEditorAction.insertUnorderedList();
                return;
            case 20:
                this.mRichEditorAction.indent();
                return;
            case 21:
                this.mRichEditorAction.outdent();
                return;
            case 22:
                this.mRichEditorAction.insertHorizontalRule();
                return;
            case 23:
                this.mRichEditorAction.formatBlockquote();
                return;
            case 24:
                this.mRichEditorAction.formatBlockCode();
                return;
            case 25:
                this.mRichEditorAction.codeView();
                return;
            default:
                return;
        }
    }

    public void command(String str) {
        int i = AnonymousClass2.$SwitchMap$com$solotech$utilities$mricheditor$ActionType[this.mActionType.ordinal()];
    }

    public int getEnabledColor() {
        return this.enabledColor;
    }

    public void setEnabledColor(int i) {
        this.enabledColor = i;
    }

    public int getDisabledColor() {
        return this.disabledColor;
    }

    public void setDisabledColor(int i) {
        this.disabledColor = i;
    }

    public int getActivatedColor() {
        return this.activatedColor;
    }

    public void setActivatedColor(int i) {
        this.activatedColor = i;
    }

    public int getDeactivatedColor() {
        return this.deactivatedColor;
    }

    public void setDeactivatedColor(int i) {
        this.deactivatedColor = i;
    }

    public void notifyFontStyleChange(final ActionType actionType, final String str) {
        post(new Runnable() { // from class: com.docreader.docviewer.pdfcreator.pdfreader.filereader.utilities.mricheditor.ui.ActionImageView.1
            @Override // java.lang.Runnable
            public void run() {
                switch (AnonymousClass2.$SwitchMap$com$solotech$utilities$mricheditor$ActionType[actionType.ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        ActionImageView actionImageView = ActionImageView.this;
                        actionImageView.setColorFilter(ContextCompat.getColor(actionImageView.mContext, Boolean.parseBoolean(str) ? ActionImageView.this.getActivatedColor() : ActionImageView.this.getDeactivatedColor()));
                        return;
                    default:
                }
            }
        });
    }
}