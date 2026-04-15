package com.easyconnect.easyconnectap.util;

/**
 * Constants defining HTTP timeout durations (in seconds) for OkHttp client configuration.
 *
 * <p>All timeouts default to 5 seconds, suitable for local-network communication
 * with DPP configurators. Used by
 * {@link com.easyconnect.easyconnectap.network.Provider#getOkHttpClient()}.
 *
 * @see com.easyconnect.easyconnectap.network.Provider
 */
public class HttpTimeOut {

    /** Private constructor to prevent instantiation of this constants class. */
    private HttpTimeOut() {
    }

    /** TCP connection timeout in seconds. */
    public static final long CONNECTION_TIME_OUT = 5;

    /** Socket read timeout in seconds. */
    public static final long READ_TIME_OUT = 5;

    /** Socket write timeout in seconds. */
    public static final long WRITE_TIME_OUT = 5;
}
