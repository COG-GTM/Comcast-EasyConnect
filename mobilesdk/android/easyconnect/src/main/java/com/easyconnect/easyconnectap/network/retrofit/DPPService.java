package com.easyconnect.easyconnectap.network.retrofit;

import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.google.gson.JsonObject;


import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * Retrofit service interface defining the DPP configurator's REST API endpoints.
 *
 * <p>Two endpoints are used:
 * <ul>
 *   <li>{@code POST /api/v1/configurator-initiate-dpp} &mdash; sends enrollee bootstrap
 *       info to the configurator to initiate DPP provisioning</li>
 *   <li>{@code POST /api/v1/configurator-dpp-uri} &mdash; retrieves the configurator's
 *       own bootstrap URI for display as a QR code</li>
 * </ul>
 *
 * <p>Authentication headers:
 * <ul>
 *   <li>{@code X-Authorization-Token} &mdash; bearer token from a prior successful request</li>
 *   <li>{@code X-Challenge-Response} &mdash; Wi-Fi passphrase for 401 challenge flows</li>
 * </ul>
 *
 * @see DPPResponse
 * @see DPPUri
 */
public interface DPPService {

    /** Sends enrollee DPP URI without authentication. */
    @POST("/api/v1/configurator-initiate-dpp")
    Single <DPPResponse> sendDPPUri(@Body JsonObject dppJson);

    /** Sends enrollee DPP URI with a bearer token from a prior request. */
    @POST("/api/v1/configurator-initiate-dpp")
    Single <DPPResponse> sendDPPUri(@Header("X-Authorization-Token") String token,@Body JsonObject dppJson);

    /** Sends enrollee DPP URI with a Wi-Fi passphrase challenge response. */
    @POST("/api/v1/configurator-initiate-dpp")
    Single <DPPResponse> sendChallenge(@Header("X-Challenge-Response") String challengeResponse, @Body JsonObject dppJson);

    /** Retrieves the configurator's bootstrap DPP URI. */
    @POST("/api/v1/configurator-dpp-uri")
    Single <DPPUri> getDPPUrii();

    /** Retrieves the configurator's DPP URI with a challenge response. */
    @POST("/api/v1/configurator-dpp-uri")
    Single <DPPUri> getDPPUrii(@Header("X-Challenge-Response") String challengeResponse);
}
