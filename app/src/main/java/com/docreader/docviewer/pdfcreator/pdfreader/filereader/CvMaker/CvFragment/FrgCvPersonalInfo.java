package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.PersonalInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.TextChangeListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgCvPersonalInfo extends ResumeFragment {
    public String[] PERMISSIONS_LIST = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public ImageView clearImage;
    public SharedPrefs prefs;
    public CircleImageView profile_image;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data;
        if (activityResult.getResultCode() == -1 && (data = activityResult.getData()) != null) {
            Uri data2 = data.getData();
            prefs.setProfilePic(data2.toString());
            Glide.with(requireActivity()).load(data2).into(profile_image);
            clearImage.setVisibility(View.VISIBLE);
        }
    });

    public void onResult() {
    }

    public static ResumeFragment newInstance(Resume resume) {
        FrgCvPersonalInfo fragmentCvPersonalInfo = new FrgCvPersonalInfo();
        fragmentCvPersonalInfo.setResume(resume);
        return fragmentCvPersonalInfo;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_personal_info, viewGroup, false);

        prefs = new SharedPrefs(requireActivity());

        profile_image = inflate.findViewById(R.id.profile_image);
        clearImage = inflate.findViewById(R.id.clearImage);

        final PersonalInfo personalInfo = getResume().personalInfo;

        EditText editText = inflate.findViewById(R.id.fNameEt);
        editText.setHint(personalInfo.getfName());
        editText.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setfName(charSequence.toString());
            }
        });

        EditText editText2 = inflate.findViewById(R.id.lastNameEt);
        editText2.setHint(personalInfo.getlName());
        editText2.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setlName(charSequence.toString());
            }
        });

        EditText editText3 = inflate.findViewById(R.id.dobEt);
        editText3.setHint(personalInfo.getDob());
        editText3.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setDob(charSequence.toString());
            }
        });

        EditText editText4 = inflate.findViewById(R.id.maritalStatusEt);
        editText4.setHint(personalInfo.getMaritalStatus());
        editText4.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setMaritalStatus(charSequence.toString());
            }
        });

        EditText editText5 = inflate.findViewById(R.id.professionTitleEt);
        editText5.setHint(personalInfo.getJobTitle());
        editText5.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setJobTitle(charSequence.toString());
            }
        });

        EditText editText6 = inflate.findViewById(R.id.address1Et);
        editText6.setHint(personalInfo.getAddressLine1());
        editText6.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setAddressLine1(charSequence.toString());
            }
        });

        EditText editText7 = inflate.findViewById(R.id.address2Et);
        editText7.setHint(personalInfo.getAddressLine2());
        editText7.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setAddressLine2(charSequence.toString());
            }
        });

        EditText editText8 = inflate.findViewById(R.id.phoneEt);
        editText8.setHint(personalInfo.getPhone());
        editText8.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setPhone(charSequence.toString());
            }
        });

        EditText editText9 = inflate.findViewById(R.id.emailET);
        editText9.setHint(personalInfo.getEmail());
        editText9.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setEmail(charSequence.toString());
            }
        });

        EditText editText10 = inflate.findViewById(R.id.summaryEt);
        editText10.setHint(personalInfo.getSummary());
        editText10.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                personalInfo.setSummary(charSequence.toString());
            }
        });

        profile_image.setOnClickListener(view -> {
            if (hasReadStoragePermission()) {
                pickImage();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_LIST, 112);
            }
        });

        setProfilePicture();

        clearImage.setOnClickListener(view -> {
            profile_image.setImageResource(R.drawable.icon_user_profile);
            prefs.setProfilePic(null);
        });

        return inflate;
    }

    @SuppressLint("IntentReset")
    public void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{ContentTypes.IMAGE_JPEG, ContentTypes.IMAGE_PNG});
        someActivityResultLauncher.launch(intent);
    }

    public boolean hasReadStoragePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int checkSelfPermission = ActivityCompat.checkSelfPermission(requireActivity(), "android.permission.READ_EXTERNAL_STORAGE");
        int checkSelfPermission2 = ActivityCompat.checkSelfPermission(requireActivity(), "android.permission.WRITE_EXTERNAL_STORAGE");
        return checkSelfPermission == 0 && checkSelfPermission2 == 0;
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 112 && iArr.length > 0) {
            boolean z = false;
            boolean z2 = iArr[0] == 0;
            if (iArr[1] == 0) {
                z = true;
            }
            if (!z2 || !z) {
                Utility.Toast(getActivity(), requireActivity().getResources().getString(R.string.permission_denied_message2));
            } else {
                pickImage();
            }
        }
    }

    public String getFileToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setProfilePicture() {
        if (prefs.getProfilePic() != null) {
            Uri parse = Uri.parse(prefs.getProfilePic());
            Utility.logCatMsg("uri " + parse.toString());
            Glide.with(requireActivity()).load(parse).into(profile_image);
            clearImage.setVisibility(View.VISIBLE);
        }
    }
}
