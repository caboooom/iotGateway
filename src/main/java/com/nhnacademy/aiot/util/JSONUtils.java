package com.nhnacademy.aiot.util;

import java.io.File;
import java.io.Reader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private JSONUtils() {
    }

    public static boolean isJson(String jsonString) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
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
            log.info(e.getMessage());
            return null;
        }
    }

    public static JsonNode parseJson(File jsonFile) {
        try {
            return objectMapper.readTree(jsonFile);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public static JsonNode parseJson(Reader reader) {
        try {
            return objectMapper.readTree(reader);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

}