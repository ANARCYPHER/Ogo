package com.cscodetech.urbantaxi.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.imagepicker.ImageCompressionListener;
import com.cscodetech.urbantaxi.imagepicker.ImagePicker;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.cscodetech.urbantaxi.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.img_profile)
    public CircleImageView imgProfile;
    @BindView(R.id.img_edit)
    public ImageView imgEdit;
    @BindView(R.id.ed_firstnamel)
    public TextView edFirstnamel;
    @BindView(R.id.ed_phone)
    public TextView edPhone;
    @BindView(R.id.ed_eamil)
    public TextView edEamil;
    @BindView(R.id.ed_address)
    public TextView edAddress;
    @BindView(R.id.txt_teram)
    public TextView txtTeram;
    @BindView(R.id.radioM)
    public RadioButton radioM;
    @BindView(R.id.radioF)
    public RadioButton radioF;
    @BindView(R.id.radioGrp)
    public RadioGroup radioGrp;
    @BindView(R.id.img_frount)
    public ImageView imgFrount;
    @BindView(R.id.img_backend)
    public ImageView imgBackend;
    @BindView(R.id.img_frountl)
    public ImageView imgFrountl;
    @BindView(R.id.img_backendl)
    public ImageView imgBackendl;

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_edit)
    public TextView txtEdit;
    SessionManager sessionManager;
    RiderData riderData;
    ImagePicker imagePicker;
    String selectImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        imagePicker = new ImagePicker();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);


        sessionManager = new SessionManager(this);
        riderData = sessionManager.getUserDetails();
        edFirstnamel.setText("" + riderData.getFname() + " " + riderData.getLname());
        edPhone.setText(riderData.getCcode() + "" + riderData.getMobile());
        edEamil.setText(riderData.getEmail());
        edAddress.setText(riderData.getAddress());

        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getProImg()).into(imgProfile);
        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getIdentityFront()).into(imgFrount);
        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getIdentityBack()).into(imgBackend);
        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getLicenseFront()).into(imgFrountl);
        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getLicenseBack()).into(imgBackendl);


    }

    @OnClick({R.id.img_back, R.id.txt_edit, R.id.btn_logout})

    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_edit:
                Utility.bottonConfirm(this, imagePicker);
                break;
            case R.id.btn_logout:
                sessionManager.logoutUser();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {
            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        selectImage = filePath;

                        Glide.with(ProfileActivity.this)
                                .load(filePath)
                                .into(imgProfile);

                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                selectImage = filePath;
                Glide.with(ProfileActivity.this)
                        .load(filePath)
                        .into(imgProfile);

            }

        }
    }
}