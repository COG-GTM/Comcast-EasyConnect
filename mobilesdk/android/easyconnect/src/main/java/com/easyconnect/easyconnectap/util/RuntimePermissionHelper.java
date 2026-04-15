package com.easyconnect.easyconnectap.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.easyconnect.easyconnectapp.R;

import java.util.ArrayList;

/**
 * Singleton helper that manages Android M+ runtime permission requests for the
 * Easy Connect application.
 *
 * <p>Tracks required permissions (currently {@link android.Manifest.permission#ACCESS_FINE_LOCATION}),
 * checks their grant status, and shows a rationale dialog before requesting if the
 * system recommends it. If permissions are denied, displays a toast and finishes
 * the activity.
 *
 * <p>Used by {@link com.easyconnect.easyconnectap.scan.ScanQRCode} to request
 * camera permission and by the main activity for location permission (required
 * for mDNS discovery on Android 8+).
 *
 * @see ScanQRCode
 */
public final class RuntimePermissionHelper {

    private static RuntimePermissionHelper runtimePermissionHelper;
    public static final int PERMISSION_REQUEST_CODE = 1;
    private Activity activity;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private ArrayList<String> requiredPermissions;
    private ArrayList<String> ungrantedPermissions = new ArrayList<String>();

    private RuntimePermissionHelper(Activity activity)  {
        this.activity = activity;
    }

    public static synchronized RuntimePermissionHelper getInstance(Activity activity){
        if(runtimePermissionHelper == null) {
            runtimePermissionHelper = new RuntimePermissionHelper(activity);
        }
        return runtimePermissionHelper;
    }

    private void initPermissions() {
        requiredPermissions = new ArrayList<String>();
        requiredPermissions.add(PERMISSION_ACCESS_FINE_LOCATION);
        //Add all the required permission in the list
    }

    /**
     * Requests all ungranted permissions, showing a rationale dialog first if
     * the system recommends it.
     */
    public void requestPermissionsIfDenied(){
        ungrantedPermissions = getUnGrantedPermissionsList();
        if(canShowPermissionRationaleDialog()){
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermissions();
                        }
                    });
            return;
        }
        askPermissions();
    }

    /**
     * Requests a single permission, showing a rationale dialog first if
     * the system recommends it.
     *
     * @param permission the Manifest permission string to request
     */
    public void requestPermissionIfDenied(final String permission){
        if(canShowPermissionRationaleDialog(permission)){
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermission(permission);
                        }
                    });
            return;
        }
        askPermission(permission);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean canShowPermissionRationaleDialog() {
        boolean shouldShowRationale = false;
        for(String permission: ungrantedPermissions) {
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if(shouldShow) {
                shouldShowRationale = true;
            }
        }
        return shouldShowRationale;
    }

    public boolean canShowPermissionRationaleDialog(String permission) {
        boolean shouldShowRationale = false;
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if(shouldShow) {
                shouldShowRationale = true;
        }
        return shouldShowRationale;
    }

    private void askPermissions() {
        if(ungrantedPermissions.size()>0) {
            ActivityCompat.requestPermissions(activity, ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    private void askPermission(String permission) {
            ActivityCompat.requestPermissions(activity, new String[] {permission}, PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(activity, R.string.permission_message, Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                })
                .create()
                .show();
    }


    /**
     * Checks whether all required permissions are currently granted.
     *
     * @return {@code true} if every required permission is granted
     */
    public boolean isAllPermissionAvailable() {
        boolean isAllPermissionAvailable = true;
        initPermissions();
        for(String permission : requiredPermissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                isAllPermissionAvailable = false;
                break;
            }
        }
        return isAllPermissionAvailable;
    }

    public ArrayList<String> getUnGrantedPermissionsList() {
        ArrayList<String> list = new ArrayList<String>();
        for(String permission: requiredPermissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if(result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    /**
     * Checks whether a specific permission is currently granted.
     *
     * @param permission the Manifest permission string to check
     * @return {@code true} if the permission is granted
     */
    public boolean isPermissionAvailable(String permission) {
        boolean isPermissionAvailable = true;
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                isPermissionAvailable = false;
            }
        return isPermissionAvailable;
    }
}
