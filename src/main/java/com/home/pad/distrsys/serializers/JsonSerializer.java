package com.home.pad.distrsys.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class JsonSerializer {
    private static Logger logger = Logger.getLogger(JsonSerializer.class.getName());

    public static <T> String toJson(T data){
        ObjectMapper m = new ObjectMapper();
        try {
            return m.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String jsonData, Class<T> targetClass){
        ObjectMapper m = new ObjectMapper();
        try {
            return m.readValue(jsonData, targetClass);
        } catch (IOException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        }
        return null;
    }
}
