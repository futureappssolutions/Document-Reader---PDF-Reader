package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CropImageActivity extends BaseActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    private Uri mCropImageUri;
    private CropImageView mCropImageView;
    private CropImageOptions mOptions;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.crop_image_activity);
        
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(100);

        ((TextView) findViewById(R.id.toolBarTitle)).setText("Crop Image");
        
        mCropImageView = findViewById(R.id.cropImageView);

        Bundle bundleExtra = getIntent().getBundleExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE);
        mCropImageUri = bundleExtra.getParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE);
        mOptions = bundleExtra.getParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS);
        if (bundle == null) {
            if (mCropImageUri == null || mCropImageUri.equals(Uri.EMPTY)) {
                if (CropImage.isExplicitCameraPermissionRequired(this)) {
                    requestPermissions(new String[]{"android.permission.CAMERA"}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                    return;
                }
                CropImage.startPickImageActivity(this);
            } else if (CropImage.isReadExternalStoragePermissionsRequired(this, mCropImageUri)) {
                requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 201);
            } else {
                mCropImageView.setImageUriAsync(mCropImageUri);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnCropImageCompleteListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_image_menu, menu);
        if (!mOptions.allowRotation) {
            menu.removeItem(R.id.crop_image_menu_rotate_left);
            menu.removeItem(R.id.crop_image_menu_rotate_right);
        } else if (mOptions.allowCounterRotation) {
            menu.findItem(R.id.crop_image_menu_rotate_left).setVisible(true);
        }
        if (!mOptions.allowFlipping) {
            menu.removeItem(R.id.crop_image_menu_flip);
        }
        if (mOptions.cropMenuCropButtonTitle != null) {
            menu.findItem(R.id.crop_image_menu_crop).setTitle(mOptions.cropMenuCropButtonTitle);
        }
        Drawable drawable = null;
        try {
            if (mOptions.cropMenuCropButtonIcon != 0) {
                drawable = ContextCompat.getDrawable(this, mOptions.cropMenuCropButtonIcon);
                menu.findItem(R.id.crop_image_menu_crop).setIcon(drawable);
            }
        } catch (Exception e) {
            Log.w("AIC", "Failed to read menu crop drawable", e);
        }
        if (mOptions.activityMenuIconColor != 0) {
            updateMenuItemIconColor(menu, R.id.crop_image_menu_rotate_left, mOptions.activityMenuIconColor);
            updateMenuItemIconColor(menu, R.id.crop_image_menu_rotate_right, mOptions.activityMenuIconColor);
            updateMenuItemIconColor(menu, R.id.crop_image_menu_flip, mOptions.activityMenuIconColor);
            if (drawable != null) {
                updateMenuItemIconColor(menu, R.id.crop_image_menu_crop, mOptions.activityMenuIconColor);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.crop_image_menu_crop) {
            cropImage();
            return true;
        } else if (menuItem.getItemId() == R.id.crop_image_menu_rotate_left) {
            rotateImage(-mOptions.rotationDegrees);
            return true;
        } else if (menuItem.getItemId() == R.id.crop_image_menu_rotate_right) {
            rotateImage(mOptions.rotationDegrees);
            return true;
        } else if (menuItem.getItemId() == R.id.crop_image_menu_flip_horizontally) {
            mCropImageView.flipImageHorizontally();
            return true;
        } else if (menuItem.getItemId() == R.id.crop_image_menu_flip_vertically) {
            mCropImageView.flipImageVertically();
            return true;
        } else if (menuItem.getItemId() == R.id.crop_image_menu_donot_crop) {
            setResultCancel();
            return true;
        } else if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            setResultCancel();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResultCancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 200) {
            if (i2 == 0) {
                setResultCancel();
            }
            if (i2 == -1) {
                Uri pickImageResultUri = CropImage.getPickImageResultUri(this, intent);
                mCropImageUri = pickImageResultUri;
                if (CropImage.isReadExternalStoragePermissionsRequired(this, pickImageResultUri)) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 201);
                    return;
                }
                mCropImageView.setImageUriAsync(mCropImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 201) {
            Uri uri = mCropImageUri;
            if (uri == null || iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, R.string.crop_image_activity_no_permissions, 1).show();
                setResultCancel();
            } else {
                mCropImageView.setImageUriAsync(uri);
            }
        }
        if (i == 2011) {
            CropImage.startPickImageActivity(this);
        }
    }

    public void onSetImageUriComplete(CropImageView cropImageView, Uri uri, Exception exc) {
        if (exc == null) {
            if (mOptions.initialCropWindowRectangle != null) {
                mCropImageView.setCropRect(mOptions.initialCropWindowRectangle);
            }
            if (mOptions.initialRotation > -1) {
                mCropImageView.setRotatedDegrees(mOptions.initialRotation);
                return;
            }
            return;
        }
        setResult(null, exc, 1);
    }

    public void onCropImageComplete(CropImageView cropImageView, CropImageView.CropResult cropResult) {
        setResult(cropResult.getUri(), cropResult.getError(), cropResult.getSampleSize());
    }

    public void cropImage() {
        if (mOptions.noOutputImage) {
            setResult(null, null, 1);
            return;
        }
        mCropImageView.saveCroppedImageAsync(getOutputUri(), mOptions.outputCompressFormat, mOptions.outputCompressQuality, mOptions.outputRequestWidth, mOptions.outputRequestHeight, mOptions.outputRequestSizeOptions);
    }

    public void rotateImage(int i) {
        mCropImageView.rotateImage(i);
    }

    public Uri getOutputUri() {
        String str;
        Uri uri = mOptions.outputUri;
        if (uri != null && !uri.equals(Uri.EMPTY)) {
            return uri;
        }
        try {
            if (mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG) {
                str = ".jpg";
            } else {
                str = mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
            }
            return Uri.fromFile(File.createTempFile("cropped", str, Utility.getTempFolderDirectory(this)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file for output image", e);
        }
    }

    public void setResult(Uri uri, Exception exc, int i) {
        setResult(exc == null ? -1 : 204, getResultIntent(uri, exc, i));
        finish();
    }

    public void setResultCancel() {
        setResult(0);
        finish();
    }

    public Intent getResultIntent(Uri uri, Exception exc, int i) {
        CropImage.ActivityResult activityResult = new CropImage.ActivityResult(mCropImageView.getImageUri(), uri, exc, mCropImageView.getCropPoints(), mCropImageView.getCropRect(), mCropImageView.getRotatedDegrees(), mCropImageView.getWholeImageRect(), i);
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, activityResult);
        return intent;
    }

    private void updateMenuItemIconColor(Menu menu, int i, int i2) {
        Drawable icon;
        MenuItem findItem = menu.findItem(i);
        if (findItem != null && (icon = findItem.getIcon()) != null) {
            try {
                icon.mutate();
                icon.setColorFilter(i2, PorterDuff.Mode.SRC_ATOP);
                findItem.setIcon(icon);
            } catch (Exception e) {
                Log.w("AIC", "Failed to update menu item color", e);
            }
        }
    }
}
