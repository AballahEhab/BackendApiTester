package com.abdallahehab.backendapitester.data;

public class HeaderField {

    private String key,value;

    public HeaderField(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
