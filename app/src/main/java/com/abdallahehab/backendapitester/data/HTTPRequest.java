package com.abdallahehab.backendapitester.data;

import java.util.ArrayList;
import java.util.Map;

public class HTTPRequest {
    public static final String POST = "POST";
    public static final String GET = "GET";

    private String urlAddress,
            requestType,
            body;

    public HTTPRequest(String urlAddress, String requestType, String body, ArrayList<HeaderField> requestHeaderFields) {
        this.urlAddress = urlAddress;
        this.requestType = requestType;
        this.body = body;
        this.requestHeaderFields = requestHeaderFields;
    }

    public HTTPRequest(String urlAddress, String requestType) {
        this.urlAddress = urlAddress;
        this.requestType = requestType;
    }

    public HTTPRequest(String urlAddress, String requestType, String body) {
        this.urlAddress = urlAddress;
        this.requestType = requestType;
        this.body = body;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<HeaderField> getRequestHeaderFields() {
        return requestHeaderFields;
    }

    private ArrayList<HeaderField> requestHeaderFields;
}
