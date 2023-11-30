package com.nhnacademy.aiot.util;

public enum PropertiesKeys {
    APPLICATION_NAME("applicationName"),
    SENSOR_TYPES("sensorTypes");

    String key;
    

    PropertiesKeys(String key){
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
