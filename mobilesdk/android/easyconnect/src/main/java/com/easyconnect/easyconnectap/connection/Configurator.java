package com.easyconnect.easyconnectap.connection;

/**
 * Immutable data class representing a resolved DPP configurator (access point)
 * discovered via mDNS service discovery.
 *
 * <p>Holds the configurator's advertised service name, resolved IP address,
 * and port number. Created by {@link NSDDiscover#getResolveListener} after
 * a successful NSD service resolution and delivered to the caller via
 * {@link IConfigurator#getselectedConfigurator(Configurator)}.
 *
 * @see NSDDiscover
 * @see IConfigurator
 */
public class Configurator {

    private String configName;
    private String configIP;
    private int configPort;

    /**
     * Creates a new Configurator with the given network details.
     *
     * @param configName the mDNS-advertised service name
     * @param configIP   the resolved IP address (e.g., "/10.0.0.1")
     * @param configPort the resolved port number
     */
    public Configurator(String configName, String configIP, int configPort) {
        this.configName = configName;
        this.configIP = configIP;
        this.configPort = configPort;
    }

    /** Returns the mDNS-advertised service name of this configurator. */
    public String getConfigName() {
        return configName;
    }

    /** Returns the resolved IP address string (may include leading slash). */
    public String getConfigIP() {
        return configIP;
    }

    /** Returns the resolved port number for REST API communication. */
    public int getConfigPort() {
        return configPort;
    }
}
