package com.easyconnect.easyconnectap.scan;

/**
 * Callback interface for receiving a QR code scan result.
 *
 * <p>Implemented by {@link com.easyconnect.easyconnectapp.app.view.MainActivity}
 * and invoked by {@link ScanQRCode#handleResult} when a QR code containing
 * a DPP URI has been successfully decoded.
 *
 * @see ScanQRCode
 */
public interface IScanResult {

    /**
     * Called when a QR code has been successfully scanned and decoded.
     *
     * @param dppUri the decoded DPP URI string from the QR code
     */
    public void getScanResult(String dppUri);
}
