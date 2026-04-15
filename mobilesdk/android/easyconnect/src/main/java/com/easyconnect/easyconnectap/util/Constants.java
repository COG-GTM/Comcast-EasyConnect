package com.easyconnect.easyconnectap.util;

/**
 * Application-wide constants for the Wi-Fi Easy Connect SDK.
 *
 * @see com.easyconnect.easyconnectap.connection.NSDDiscover
 */
public class Constants {

    /** Private constructor to prevent instantiation of this constants class. */
    private Constants() {
    }

    /**
     * mDNS service type for DPP-capable configurators.
     * Matches the Wi-Fi Alliance's standard service advertisement.
     */
    public static final String SERVICE_TYPE ="_dpp._tcp.";
}
