package com.nhnacademy.aiot.enums;

import lombok.Getter;

/**
 *
 */

@Getter
public enum CmdOptions {
    
    CONFIG_FILE( "configFile", "c" ,"Argument는 ','로 구분된 문자열로 주어짐"),
    SENSOR_TYPES("sensorTypes", "s" , "JSON 형식의 설정파일을 argument로 받는다."),
    APPLICATION_NAME("applicationName", "an", null);

    String key;
    String value;
    String description;

    CmdOptions(String key, String value, String description){
        this.key = key;
        this.value = value;
        this.description = description;

    }

}
