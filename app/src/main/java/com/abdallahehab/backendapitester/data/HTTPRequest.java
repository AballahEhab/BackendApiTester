package com.abdallahehab.backendapitester.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class HTTPRequest {
    public static final String POST = "POST";
    public static final String GET = "GET";

    private String requestType, body;

    private URL url ;

    private ArrayList<HeaderField> requestHeaderFields;


    public HTTPRequest(String urlAddress, String requestType, String body, ArrayList<HeaderField> requestHeaderFields) throws MalformedURLException {
        this.requestType = requestType;
        this.body = body;
        this.requestHeaderFields = requestHeaderFields;
        url = new URL(urlAddress);
    }

    public HTTPRequest(String urlAddress, String requestType) throws MalformedURLException {
        url = new URL(urlAddress);
        this.requestType = requestType;
    }

    public HTTPRequest(String urlAddress, String requestType, String body) throws MalformedURLException {
        url = new URL(urlAddress);
        this.requestType = requestType;
        this.body = body;
    }

    public URL getUrl() {
        return url;
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

}
