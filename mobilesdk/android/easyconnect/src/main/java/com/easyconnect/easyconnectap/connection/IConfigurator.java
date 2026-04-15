package com.easyconnect.easyconnectap.connection;

import android.net.nsd.NsdServiceInfo;

import java.util.List;

/**
 * Callback interface for receiving a resolved DPP configurator selection.
 *
 * <p>Implemented by {@link com.easyconnect.easyconnectapp.app.view.MainActivity}
 * and invoked by {@link NSDDiscover#getResolveListener} after a user-selected
 * mDNS service has been resolved to a concrete IP address and port.
 *
 * @see Configurator
 * @see NSDDiscover
 */
public interface IConfigurator {

    /**
     * Called when a configurator's network details have been resolved.
     *
     * @param configurator the resolved configurator with name, IP, and port
     */
    public void getselectedConfigurator(Configurator configurator);
}
