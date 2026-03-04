package com.easyconnect.easyconnectap.scan;

/**
 * Functional interface to pass scanned dpp uri.
 * Can be used as a lambda expression target in Java 8+.
 */
@FunctionalInterface
public interface IScanResult {

    void getScanResult(String dppUri);
}
