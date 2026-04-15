package com.easyconnect.easyconnectap.scan;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Singleton utility that generates QR code {@link Bitmap} images from DPP URI strings.
 *
 * <p>Uses the ZXing {@link MultiFormatWriter} to encode a DPP URI into a 400x400
 * pixel {@link BarcodeFormat#QR_CODE} matrix, then converts it to an Android
 * {@link Bitmap} via the Journeyapps {@link BarcodeEncoder}.
 *
 * <p>Used by {@link com.easyconnect.easyconnectapp.app.view.QRCodeActivity} to
 * display the configurator's bootstrap URI as a scannable QR code.
 *
 * @see ScanQRCode
 */
public class GenerateQRCode {

    private static GenerateQRCode generateQRCode;

    /**
     * To get the instance of GenerateQRCode
     */
    public static GenerateQRCode getInstance(){

        if(generateQRCode == null){

            generateQRCode = new GenerateQRCode();
        }

        return generateQRCode;
    }

    /**
     * Encodes a DPP URI string into a 400x400 pixel QR code bitmap.
     *
     * @param dppUrl the DPP URI string to encode (e.g., {@code "DPP:C:81/1;M:...;K:...;;"})
     * @return the generated QR code as a {@link Bitmap}, or {@code null} if encoding fails
     */
    public Bitmap getQRCodeFromDPP(String dppUrl){
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(dppUrl, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
