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
 * Util Class to check runtime permissions
 */
public final class RuntimePermissionHelper {

    private static RuntimePermissionHelper runtimePermissionHelper;
    public static final int PERMISSION_REQUEST_CODE = 1;
    private Activity activity;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private ArrayList<String> requiredPermissions;
    // Java 8: Using diamond operator for type inference
    private ArrayList<String> ungrantedPermissions = new ArrayList<>();

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
        // Java 8: Using diamond operator for type inference
        requiredPermissions = new ArrayList<>();
        requiredPermissions.add(PERMISSION_ACCESS_FINE_LOCATION);
        //Add all the required permission in the list
    }

    public void requestPermissionsIfDenied(){
        ungrantedPermissions = getUnGrantedPermissionsList();
        if(canShowPermissionRationaleDialog()){
            // Java 8: Replaced anonymous inner class with lambda expression
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    (dialog, which) -> askPermissions());
            return;
        }
        askPermissions();
    }

    public void requestPermissionIfDenied(final String permission){
        if(canShowPermissionRationaleDialog(permission)){
            // Java 8: Replaced anonymous inner class with lambda expression
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    (dialog, which) -> askPermission(permission));
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
                // Java 8: Replaced anonymous inner class with lambda expression
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    Toast.makeText(activity, R.string.permission_message, Toast.LENGTH_SHORT).show();
                    activity.finish();
                })
                .create()
                .show();
    }


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
        // Java 8: Using diamond operator for type inference
        ArrayList<String> list = new ArrayList<>();
        for(String permission: requiredPermissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if(result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    public boolean isPermissionAvailable(String permission) {
        boolean isPermissionAvailable = true;
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                isPermissionAvailable = false;
            }
        return isPermissionAvailable;
    }
}
