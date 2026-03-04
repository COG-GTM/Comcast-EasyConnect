package com.easyconnect.easyconnectap.connection;

import android.net.nsd.NsdServiceInfo;

import java.util.List;

/**
 * Functional interface to pass discovered NSDService List.
 * Can be used as a lambda expression target in Java 8+.
 */
@FunctionalInterface
public interface IMDNSDiscovery {

    void mDNSConfigList(List<NsdServiceInfo> mDnsConfigList);
}
