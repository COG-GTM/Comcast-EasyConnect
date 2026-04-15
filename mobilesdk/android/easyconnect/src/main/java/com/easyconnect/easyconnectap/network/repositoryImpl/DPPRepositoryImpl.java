package com.easyconnect.easyconnectap.network.repositoryImpl;

import com.easyconnect.easyconnectap.errorhandling.HttpErrorRetryChecker;
import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.easyconnect.easyconnectap.network.repository.DPPRepository;
import com.easyconnect.easyconnectap.network.retrofit.DPPService;
import com.google.gson.JsonObject;
import io.reactivex.Single;

/**
 * Concrete implementation of {@link DPPRepository} backed by Retrofit's {@link DPPService}.
 *
 * <p>Each repository method wraps the corresponding Retrofit call in a deferred
 * {@link Single} and applies an {@link HttpErrorRetryChecker} that automatically
 * retries on HTTP 401 (Unauthorized) up to 3 times before propagating the error.
 *
 * <p>Instantiated by {@link com.easyconnect.easyconnectap.network.Provider#getDPPRepository(String)}
 * with a {@link DPPService} created from the configurator's base URL.
 *
 * @see DPPRepository
 * @see DPPService
 * @see HttpErrorRetryChecker
 */
public class DPPRepositoryImpl implements DPPRepository {

    private DPPService dppService;

    /**
     * @param dppService the Retrofit service interface for DPP API calls
     */
    public DPPRepositoryImpl(DPPService dppService) {
        this.dppService = dppService;
    }

    @Override
    public Single<DPPResponse> getDPPResponse(JsonObject dppjson) {
        return Single.defer(() -> {
                return dppService.sendDPPUri(dppjson);
        }).retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPResponse> getDPPResponse(String token, JsonObject dppJson) {
        return Single.defer(() -> {
            return dppService.sendDPPUri(token,dppJson);
        }).retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPResponse> sendChallengeResponse(String challengeResponse, JsonObject dppJson) {
        return  Single.defer(() -> {
            return dppService.sendChallenge(challengeResponse,dppJson);
        }).retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPUri> getDPPUri() {
        return Single.defer(() -> {
            return dppService.getDPPUrii();
        }).retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPUri> getDPPUri(String challengeResponse) {
        return Single.defer(() -> {

            return dppService.getDPPUrii(challengeResponse);

        }).retryWhen(new HttpErrorRetryChecker());
    }
}
