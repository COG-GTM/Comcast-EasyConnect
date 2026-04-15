package com.easyconnect.easyconnectapp.app.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.easyconnect.easyconnectap.scan.GenerateQRCode;
import com.easyconnect.easyconnectap.util.SharedPrefsUtils;
import com.easyconnect.easyconnectapp.app.R;


/**
 * Activity that displays the configurator's DPP URI as a scannable QR code.
 *
 * <p>Launched from {@link MainActivity} after the configurator's bootstrap URI has been
 * successfully retrieved via {@code GET /api/v1/configurator-dpp-uri} and stored in
 * {@link SharedPrefsUtils} under the {@code dpp_for_qrcode} key.
 *
 * <p>Uses {@link GenerateQRCode} to encode the DPP URI string into a 400x400 pixel
 * QR code bitmap, which is rendered in an {@link ImageView}.
 *
 * @see MainActivity#getDPPUriFromServer()
 * @see GenerateQRCode#getQRCodeFromDPP(String)
 */
public class QRCodeActivity extends AppCompatActivity {

    /**
     * Retrieves the stored DPP URI from SharedPreferences and renders it as a QR code.
     *
     * <p>If the URI is {@code null} (e.g., cleared between activities), a toast error
     * is shown instead of a QR code.
     *
     * @param savedInstanceState the previously saved instance state, or {@code null}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        ImageView qrImageview = (ImageView) findViewById(R.id.img_qrcode);

        String dppURI = SharedPrefsUtils.getInstance().getStringPreference(this, getResources().getString(R.string.dpp_for_qrcode));

        if(dppURI!=null) {
            qrImageview.setImageBitmap(GenerateQRCode.getInstance().getQRCodeFromDPP(dppURI));
        } else{
            Toast.makeText(this, "Unable to get QRCode from DPP URI", Toast.LENGTH_SHORT).show();
        }
    }
}
