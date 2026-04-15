package com.easyconnect.easyconnectap.network.repository;

import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.google.gson.JsonObject;

import io.reactivex.Single;


/**
 * Repository interface defining the DPP REST API operations available to the
 * configurator. All methods return RxJava {@link Single} observables for
 * asynchronous execution.
 *
 * <p>Concrete implementation: {@link com.easyconnect.easyconnectap.network.repositoryImpl.DPPRepositoryImpl}
 *
 * @see DPPResponse
 * @see DPPUri
 */
public interface DPPRepository {

    /**
     * Sends an enrollee's DPP URI to the configurator without authentication.
     *
     * @param dppJson JSON body containing {@code "dpp_uri"} field
     * @return a {@link Single} emitting the configurator's response
     */
    Single<DPPResponse> getDPPResponse(JsonObject dppJson);

    /**
     * Sends an enrollee's DPP URI to the configurator with a bearer token.
     *
     * @param token   the {@code X-Authorization-Token} from a prior successful request
     * @param dppJson JSON body containing {@code "dpp_uri"} field
     * @return a {@link Single} emitting the configurator's response
     */
    Single<DPPResponse> getDPPResponse(String token,JsonObject dppJson);

    /**
     * Sends a challenge response (Wi-Fi passphrase) to the configurator
     * after receiving a 401 from the initiation endpoint.
     *
     * @param challengeResponse the user-entered passphrase
     * @param dppJson           JSON body containing {@code "dpp_uri"} field
     * @return a {@link Single} emitting the configurator's response
     */
    Single<DPPResponse> sendChallengeResponse(String challengeResponse,JsonObject dppJson);

    /**
     * Retrieves the configurator's own DPP URI for QR code display.
     *
     * @return a {@link Single} emitting the DPP URI response
     */
    Single<DPPUri> getDPPUri();

    /**
     * Retrieves the configurator's DPP URI with a challenge response header.
     *
     * @param challengeResponse the user-entered passphrase
     * @return a {@link Single} emitting the DPP URI response
     */
    Single<DPPUri> getDPPUri(String challengeResponse);
 }
