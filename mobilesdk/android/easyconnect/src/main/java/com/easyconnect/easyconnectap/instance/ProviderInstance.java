package com.easyconnect.easyconnectap.instance;

import com.easyconnect.easyconnectap.network.Provider;

/**
 * Singleton accessor for the {@link Provider} instance that supplies Retrofit,
 * OkHttp, and {@link com.easyconnect.easyconnectap.network.repository.DPPRepository}
 * objects throughout the application.
 *
 * <p>Provides a simple lazy-initialization pattern. The {@link Provider} is
 * created on first access and reused for all subsequent calls.
 *
 * @see Provider
 */
public class ProviderInstance {

    private static Provider provider;

    /** Private constructor to prevent direct instantiation. */
    private ProviderInstance() {
    }

    /**
     * Returns the shared {@link Provider} instance, creating it on first access.
     *
     * @return the singleton Provider
     */
    public static Provider getProvider(){
        if(provider==null){

                provider=new Provider();
        }
        return provider;
    }
}
