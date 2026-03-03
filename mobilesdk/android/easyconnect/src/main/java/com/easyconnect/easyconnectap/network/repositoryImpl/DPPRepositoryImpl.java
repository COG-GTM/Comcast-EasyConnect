package com.easyconnect.easyconnectap.network.repositoryImpl;

import com.easyconnect.easyconnectap.errorhandling.HttpErrorRetryChecker;
import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.easyconnect.easyconnectap.network.repository.DPPRepository;
import com.easyconnect.easyconnectap.network.retrofit.DPPService;
import com.google.gson.JsonObject;
import io.reactivex.Single;

public class DPPRepositoryImpl implements DPPRepository {

    private DPPService dppService;

    public DPPRepositoryImpl(DPPService dppService) {
        this.dppService = dppService;
    }

    // Java 8: Simplified lambda expressions by removing redundant return statements
    @Override
    public Single<DPPResponse> getDPPResponse(JsonObject dppjson) {
        return Single.defer(() -> dppService.sendDPPUri(dppjson))
                .retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPResponse> getDPPResponse(String token, JsonObject dppJson) {
        return Single.defer(() -> dppService.sendDPPUri(token, dppJson))
                .retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPResponse> sendChallengeResponse(String challengeResponse, JsonObject dppJson) {
        return Single.defer(() -> dppService.sendChallenge(challengeResponse, dppJson))
                .retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPUri> getDPPUri() {
        return Single.defer(() -> dppService.getDPPUrii())
                .retryWhen(new HttpErrorRetryChecker());
    }

    @Override
    public Single<DPPUri> getDPPUri(String challengeResponse) {
        return Single.defer(() -> dppService.getDPPUrii(challengeResponse))
                .retryWhen(new HttpErrorRetryChecker());
    }
}
