package com.cscodetech.urbantaxi.utility;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.cscodetech.urbantaxi.MyApplication;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.activity.HomeActivity;
import com.cscodetech.urbantaxi.model.RiderData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



/**
 * Starts location updates on background and publish LocationUpdateEvent upon
 * each new location result.
 */
public class LocationUpdateService extends Service {

    //region data
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private FirebaseFirestore db;
    SessionManager sessionManager;
    RiderData riderData;
    public static Location currentLocation;
    //endregion

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();

        sessionManager = new SessionManager(this);
        riderData = sessionManager.getUserDetails();

        initData();
    }
    //Location Callback
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            currentLocation = locationResult.getLastLocation();
            Log.d("Locations", currentLocation.getLatitude() + "," + currentLocation.getLongitude());


            Map<String, Object> city = new HashMap<>();

            city.put("lats", currentLocation.getLatitude());
            city.put("logs", currentLocation.getLongitude());


            db.collection("Admin").document(riderData.getId())
                    .update(city)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

            //Share/Publish Location
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        prepareForegroundNotification();
        startLocationUpdates();

        return START_STICKY;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MyApplication.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, Looper.myLooper());
    }

    private void prepareForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    Utility.channel01,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        Intent notificationIntent = new Intent(MyApplication.mContext, HomeActivity.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getActivity(MyApplication.mContext,
                    Utility.serviceLocationRequestCode,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(MyApplication.mContext,
                    Utility.serviceLocationRequestCode,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }

        Notification notification = new NotificationCompat.Builder(MyApplication.mContext, Utility.channel01)
                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_notification_description))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(Utility.locationServiceNotifId, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void initData() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(MyApplication.mContext);

    }
    public static Location getLocation(){
        return currentLocation;
    }
}