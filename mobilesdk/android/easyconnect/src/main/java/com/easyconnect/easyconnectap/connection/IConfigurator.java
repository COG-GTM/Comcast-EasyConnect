package com.easyconnect.easyconnectap.connection;

/**
 * Functional interface to pass selected MDNS Configurator.
 * Can be used as a lambda expression target in Java 8+.
 */
@FunctionalInterface
public interface IConfigurator {

    void getselectedConfigurator(Configurator configurator);
}
