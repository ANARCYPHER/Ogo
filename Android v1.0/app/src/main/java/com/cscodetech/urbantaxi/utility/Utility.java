package com.cscodetech.urbantaxi.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cscodetech.urbantaxi.MyApplication;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.imagepicker.ImagePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Utility {

    public static String rtl = "rtl";

    public static int isvarification = -1;

    public static Dialog popupWindow;

    public static String channel01 = "my_channel_01";
    public static int serviceLocationRequestCode = 1010;
    public static int locationServiceNotifId = 3210;
    public static GoogleApiClient googleApiClient;

    public static void enableLoc(Activity context) {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.i("test", "-->");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })

                    .addOnConnectionFailedListener(connectionResult -> Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {// Show the dialog by calling startResolutionForResult(),
                try {
                    status.startResolutionForResult(context, 210);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean hasGPSDevice(Context context) {
        LocationManager lm = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;


        }
    }

    public static boolean internetChack() {
        ConnectivityManager ConnectionManager = (ConnectivityManager) MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }




    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void bottonConfirm(Context context, ImagePicker imagePicker) {
        Activity activity = (Activity) context;

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        View sheetView =  activity.getLayoutInflater().inflate(R.layout.select_camera_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        TextView btncamera = sheetView.findViewById(R.id.btn_camera);
        TextView btnGallery = sheetView.findViewById(R.id.btn_gallery);
        btncamera.setOnClickListener(v -> {

            mBottomSheetDialog.cancel();
        });
        btnGallery.setOnClickListener(v -> {

            mBottomSheetDialog.cancel();

        });

        mBottomSheetDialog.show();
    }
    public static String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MyApplication.mContext.getString(R.string.api_key);
        return url;
    }

    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (16 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 3;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

}
