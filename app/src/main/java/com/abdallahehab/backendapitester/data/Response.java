package com.abdallahehab.backendapitester.data;

import android.util.Log;

import java.util.List;
import java.util.Map;

public class Response {

    private int responseCode;
    private String errorMessage, responseBody,responseMessage;

    private Map<String, List<String>> responseHeaderFields;

    public Response(int responseCode, String errorMessage, String responseBody, String responseMessage, Map<String, List<String>> responseHeaderFields) {
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        this.responseBody = responseBody;
        this.responseMessage = responseMessage;
        this.responseHeaderFields = responseHeaderFields;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Map<String, List<String>> getResponseHeaderFields() {
        return responseHeaderFields;
    }

    @Override
    public String toString() {

        return "Response{" +
                "responseCode=" + responseCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", responseHeaderFields=" + responseHeaderFields +
                '}';
    }
}
