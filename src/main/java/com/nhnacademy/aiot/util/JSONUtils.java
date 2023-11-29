package com.nhnacademy.aiot.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {

    private JSONUtils(){
        
    }

    public static boolean isJson(String jsonString) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(jsonString);
            if (obj instanceof JSONObject) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }

        return false;
    }
}
