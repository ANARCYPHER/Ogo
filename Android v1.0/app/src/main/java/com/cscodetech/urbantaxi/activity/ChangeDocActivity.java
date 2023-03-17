package com.cscodetech.urbantaxi.activity;

import static com.cscodetech.urbantaxi.utility.Utility.bottonConfirm;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.imagepicker.ImageCompressionListener;
import com.cscodetech.urbantaxi.imagepicker.ImagePicker;
import com.cscodetech.urbantaxi.model.RestResponse;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.FileUtils;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ChangeDocActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_tile)
    public TextView txtTile;
    @BindView(R.id.img_frount)
    public ImageView imgFrount;
    @BindView(R.id.img_backend)
    public ImageView imgBackend;
    @BindView(R.id.txt_uploadfrount)
    public TextView txtUploadfrount;
    @BindView(R.id.lvl_click)
    public LinearLayout lvlClick;
    @BindView(R.id.btn_continue)
    public TextView btnContinue;

    ImagePicker imagePicker;
    String selectImagef;
    String selectImageb;
    int image = 0;
    String tyep;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    RiderData riderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_doc);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        riderData = sessionManager.getUserDetails();
        imagePicker = new ImagePicker();
        tyep = getIntent().getStringExtra("type");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);

    }

    @OnClick({R.id.img_back, R.id.btn_continue, R.id.img_frount, R.id.img_backend})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_continue:
                if (tyep.equalsIgnoreCase(getResources().getString(R.string.upload_identity))) {
                    uploadIdentirFile();
                } else {
                    uploadDocFile();
                }
                break;
            case R.id.img_frount:
                image = 1;
                bottonConfirm(this, imagePicker);

                break;
            case R.id.img_backend:
                image = 2;
                bottonConfirm(this, imagePicker);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }


    private void uploadIdentirFile() {


        List<MultipartBody.Part> identity_f = new ArrayList<>();
        identity_f.add(FileUtils.prepareFilePart("identity_front", selectImagef));

        List<MultipartBody.Part> identity_b = new ArrayList<>();
        identity_b.add(FileUtils.prepareFilePart("identity_back", selectImageb));


        custPrograssbar.prograssCreate(this);
        RequestBody rider_id = FileUtils.createPartFromString(riderData.getId());
        Call<JsonObject> call = APIClient.getInterface().reupIdentit(rider_id, identity_f, identity_b);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void uploadDocFile() {
        custPrograssbar.prograssCreate(this);

        List<MultipartBody.Part> identity_f = new ArrayList<>();
        identity_f.add(FileUtils.prepareFilePart("license_front", selectImagef));

        List<MultipartBody.Part> identity_b = new ArrayList<>();
        identity_b.add(FileUtils.prepareFilePart("license_back", selectImageb));

        RequestBody rider_id = FileUtils.createPartFromString(riderData.getId());

        Call<JsonObject> call = APIClient.getInterface().reuplicense(rider_id, identity_f, identity_b);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

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

                        if (image == 1) {
                            selectImagef = filePath;
                            Glide.with(ChangeDocActivity.this)
                                    .load(filePath)
                                    .into(imgFrount);
                        } else {
                            selectImageb = filePath;
                            Glide.with(ChangeDocActivity.this)
                                    .load(filePath)
                                    .into(imgBackend);
                        }


                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {

                if (image == 1) {
                    selectImagef = filePath;
                    Glide.with(ChangeDocActivity.this)
                            .load(filePath)
                            .into(imgFrount);
                } else {
                    selectImageb = filePath;
                    Glide.with(ChangeDocActivity.this)
                            .load(filePath)
                            .into(imgBackend);
                }

            }

        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            Gson gson = new Gson();
            RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
            if (restResponse.getResult().equalsIgnoreCase("true")) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}