package com.easyconnect.easyconnectap.connection;

import android.net.nsd.NsdServiceInfo;

import java.util.List;

/**
 * * @Interface to pass discovered NSDService List
 */
public interface IMDNSDiscovery {

    void mDNSConfigList(List<NsdServiceInfo> mDnsConfigList);
}
