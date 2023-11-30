package com.nhnacademy.aiot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public class Parser {
    static String json;
    static MqttMessage msg;

    public static void main(String[] args) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            // 필요한 값을 추출
            String deviceId = jsonNode.path("deviceInfo").path("devEui").asText();
            double temperature = jsonNode.path("object").path("temperature").asDouble();

            // 추출한 값 출력
            System.out.println("Device ID: " + deviceId);
            System.out.println("Temperature: " + temperature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSensor(String sensorName) throws IOException {
        ObjectMapper obj = new ObjectMapper();
        JsonNode jsonNode = obj.readTree(json);
        System.out.println(jsonNode);
    }


}

