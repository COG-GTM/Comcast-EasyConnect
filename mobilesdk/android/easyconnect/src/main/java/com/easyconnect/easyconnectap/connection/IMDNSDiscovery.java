package com.easyconnect.easyconnectap.connection;

import android.net.nsd.NsdServiceInfo;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Callback interface for receiving mDNS discovery results.
 *
 * <p>Implemented by {@link com.easyconnect.easyconnectapp.app.view.MainActivity}
 * and invoked by {@link NSDDiscover} when the mDNS scan window (10 seconds)
 * completes. The list may contain zero, one, or multiple discovered
 * configurators.
 *
 * @see NSDDiscover#DiscoverMDNSConfigurator(android.content.Context, IMDNSDiscovery)
 */
public interface IMDNSDiscovery {

    /**
     * Called when mDNS discovery has finished, delivering all discovered services.
     *
     * @param mDnsConfigList list of {@link NsdServiceInfo} for each discovered
     *                       DPP configurator; may be empty if none were found
     */
    public void mDNSConfigList(List<NsdServiceInfo> mDnsConfigList);
}
