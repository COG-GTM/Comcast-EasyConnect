package com.easyconnect.easyconnectap.util;

/**
 * Constants for HTTP status codes used in DPP API error handling.
 *
 * <p>Currently defines only 401 (Unauthorized), which triggers the
 * challenge-response passphrase flow and automatic retry logic in
 * {@link com.easyconnect.easyconnectap.errorhandling.HttpErrorRetryChecker}.
 *
 * @see com.easyconnect.easyconnectap.errorhandling.HttpErrorRetryChecker
 */
public class HttpStatusCodes {

    /** Private constructor to prevent instantiation of this constants class. */
    private HttpStatusCodes() {
    }

    /** HTTP 401 Unauthorized — configurator requires a Wi-Fi passphrase. */
    public static final int HTTP_UNAUTHORIZED = 401;
}
