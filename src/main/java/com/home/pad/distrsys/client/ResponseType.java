package com.home.pad.distrsys.client;


public enum ResponseType {
    JSON_TYPE("json"), XML_TYPE("xml");

    private String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
