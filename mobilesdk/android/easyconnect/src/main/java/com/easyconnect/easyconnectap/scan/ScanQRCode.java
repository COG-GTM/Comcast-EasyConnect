package com.easyconnect.easyconnectap.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.easyconnect.easyconnectapp.BuildConfig;
import com.easyconnect.easyconnectapp.R;
import com.google.zxing.Result;
import com.easyconnect.easyconnectap.util.Constants;
import com.easyconnect.easyconnectap.util.RuntimePermissionHelper;
import com.easyconnect.easyconnectap.util.SharedPrefsUtils;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Singleton that manages QR code scanning using the ZXing barcode library.
 *
 * <p>Handles the complete QR scan lifecycle:
 * <ol>
 *   <li>Checks (and requests) camera runtime permissions via {@link RuntimePermissionHelper}</li>
 *   <li>Starts the camera preview on the provided {@link ZXingScannerView}</li>
 *   <li>Decodes the first detected QR code into a DPP URI string</li>
 *   <li>Persists the scanned URI to {@link SharedPrefsUtils} and delivers it
 *       to the caller via {@link IScanResult#getScanResult(String)}</li>
 * </ol>
 *
 * <p>If the camera permission is denied more than twice, the user is redirected
 * to the application's system settings page.
 *
 * @see IScanResult
 * @see GenerateQRCode
 * @see RuntimePermissionHelper
 */
public class ScanQRCode implements ZXingScannerView.ResultHandler, ActivityCompat.OnRequestPermissionsResultCallback {

    private Context mContext;
    private RuntimePermissionHelper runtimePermissionHelper;
    private ZXingScannerView mScannerView;
    private int count = 0;
    private String TAG = "ScanQRCode";
    private Activity mActivity;
    private IScanResult iScanResult;
    private static ScanQRCode scanQRCode = null;

    // static method to create instance of ScanQRCode class
    public static ScanQRCode getInstance()
    {
        if (scanQRCode == null)
            scanQRCode = new ScanQRCode();

        return scanQRCode;
    }

    /**
     * Entry point to initiate a QR code scan.
     *
     * <p>Stores references to the scanner view, hosting activity, and callback,
     * then checks for camera permission before starting the camera.
     *
     * @param scannerView the ZXing scanner view embedded in the activity layout
     * @param activity    the hosting activity (needed for permission requests)
     * @param scanResult  callback to receive the decoded DPP URI string
     */
    public void scanQRCodeToGetUri(ZXingScannerView scannerView, Activity activity, IScanResult scanResult) {

        mScannerView = scannerView;
        mContext = activity;
        mActivity = activity;
        iScanResult = scanResult;

        //Runtime permission check
        askPermission();

        //Scan QR Code
        //scanQRCode(scannerView);
    }

    private void scanQRCode(ZXingScannerView scannerView) {

        scannerView.setResultHandler(this::handleResult);
        scannerView.startCamera();
    }


    /**
     * * To get runtime permission
     */
    private void askPermission() {
        //Asking runtime permission before starting camera
        if (Build.VERSION.SDK_INT >= 23) {
            runtimePermissionHelper = RuntimePermissionHelper.getInstance(mActivity);
            if (runtimePermissionHelper.isPermissionAvailable(RuntimePermissionHelper.PERMISSION_CAMERA)) {
                //Scan QR Code
                //scanQRCodetogetUri();
                //Scan QR Code
                scanQRCode(mScannerView);
            } else {
                runtimePermissionHelper.setActivity(mActivity);
                runtimePermissionHelper.requestPermissionIfDenied(RuntimePermissionHelper.PERMISSION_CAMERA);
            }
        } else {
            //Starting Camera without runtime permission
           // scanQRCodetogetUri();

            //Scan QR Code
            scanQRCode(mScannerView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionGranted = true;
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
                break;
            }
        }
        if (permissionGranted) {

            Log.d(TAG,"scannerview: "+ mScannerView);
            // Start scan once permission is granted
            scanQRCode(mScannerView);

        } else {
            if (count > 2) {
                Toast.makeText(mContext, R.string.camera_permission_toast, Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
            }
            runtimePermissionHelper.requestPermissionIfDenied(RuntimePermissionHelper.PERMISSION_CAMERA);
        }
        count++;
    }

    /**
     * Called by ZXing when a QR code is successfully decoded.
     *
     * <p>Stops the camera, persists the scanned DPP URI to SharedPreferences,
     * and delivers it to the registered {@link IScanResult} callback.
     *
     * @param result the decoded barcode result containing the DPP URI
     */
    @Override
    public void handleResult(Result result) {

        Log.d(TAG,"ScanResult: "+result.toString());
        mScannerView.stopCamera();
        //Storing scan result in SharedPrefs
        SharedPrefsUtils.getInstance().setStringPreference(mContext, mContext.getResources().getString(R.string.mdns_dpp_uri), result.toString());

        iScanResult.getScanResult(result.toString());
    }

}
