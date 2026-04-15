package com.easyconnect.easyconnectap.util;

/**
 * Simple POJO representing an HTTP error with a numeric code and descriptive message.
 *
 * <p>Used internally for error deserialization and logging. Not directly tied to
 * the DPP REST API response models ({@link com.easyconnect.easyconnectap.network.model.DPPResponse}).
 */
public class MyErrorMessage {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
