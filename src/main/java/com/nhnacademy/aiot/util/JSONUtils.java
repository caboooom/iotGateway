package com.nhnacademy.aiot.util;

import java.io.File;
import java.io.Reader;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private JSONUtils() {
    }

    public static boolean isJson(String jsonString) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    public static JsonNode parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode parseJson(File jsonFile) {
        try {
            return objectMapper.readTree(jsonFile);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode parseJson(Reader reader) {
        try {
            return objectMapper.readTree(reader);
        } catch (Exception e) {
            return null;
        }
    }

}