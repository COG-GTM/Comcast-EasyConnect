package com.easyconnect.easyconnectap.network.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for the configurator's DPP initiation response from
 * {@code POST /api/v1/configurator-initiate-dpp}.
 *
 * <p>JSON structure:
 * <pre>{@code
 * {
 *   "status":  "200",
 *   "message": "DPP process initiated",
 *   "token":   "eyJhbGciOiJIUzI1NiIs..."
 * }
 * }</pre>
 *
 * <p>The {@code token} field is nullable &mdash; present only on successful authentication.
 * When present, it should be stored and sent as an {@code X-Authorization-Token} header
 * in subsequent requests.
 *
 * @see DPPUri
 */
public class DPPResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @Nullable
    @SerializedName("token")
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
