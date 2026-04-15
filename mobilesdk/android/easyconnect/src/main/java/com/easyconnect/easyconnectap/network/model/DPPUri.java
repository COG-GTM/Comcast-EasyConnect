package com.easyconnect.easyconnectap.network.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
/**
 * Data model for the configurator's DPP URI response from
 * {@code GET /api/v1/configurator-dpp-uri}.
 *
 * <p>JSON structure:
 * <pre>{@code
 * {
 *   "status":  "200",
 *   "message": "Success",
 *   "dpp_uri": "DPP:C:81/1;M:00:c0:ca:97:64:ca;K:MDkw..."
 * }
 * }</pre>
 *
 * <p>The {@code dpp_uri} field is nullable &mdash; it is only present on success (200).
 * On error, {@code status} and {@code message} describe the failure.
 *
 * @see DPPResponse
 */
public class DPPUri {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    @Nullable
    @SerializedName("dpp_uri")
    private String dpp_uri;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDpp_uri() {
        return dpp_uri;
    }

    public void setDpp_uri(String dpp_uri) {
        this.dpp_uri = dpp_uri;
    }
}
