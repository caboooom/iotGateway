package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Wire;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Filter 는 SensorTypeFilterNode 처럼 개별로 만들지 말고,
// FilterNode를 만들고 여기에 설정을 통해서 SensorType, Branch 등을 필터링 할 수 있도록 만들어 보세요
// TODO Branch
public class FilterNode extends Node{
    String[] sensorTypes;
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode payload;

    public FilterNode(int inputPortCount, int outputPortCount, String[] sensorTypes) {
        super(inputPortCount, outputPortCount);
        this.sensorTypes = sensorTypes;
    }

    @Override
    public void process() {
        for (Wire wire : inputPorts[0].getWires()){
            if (wire.hasMessage()){
                Msg msg = wire.get();
                try {
                    processMsg(msg.toString(), sensorTypes);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void processMsg(String msgString, String[] sensorTypes) throws JsonProcessingException {
        payload = objectMapper.readTree(msgString);

        int quantity = sensorTypes.length;

        for (int i = 0; i < quantity; i++){
            Double value = payload.path("object").path(sensorTypes[i]).asDouble();
            String deviceId = getData("object", "deviceInfo", "devEui");
            String place = getData("deviceInfo", "tags", "place");

            Msg outMsg = createMessage(deviceId, place, sensorTypes[i], value);
            out(outMsg);
        }
    }

    private String getData(String... paths) {
        JsonNode result = payload;
        for (String path : paths) {
            result = result.path(path);
        }

        return result.asText();
    }


    private Msg createMessage(String deviceId, String place, String sensor, Double sensorValue){
        Msg outMsg = new Msg();
        outMsg.setTopic("/d/" + deviceId + "/p/" + place + "/e/" + sensor);

        JSONObject outPayload = new JSONObject();
        outPayload.put("time", System.currentTimeMillis());
        outPayload.put(sensor, sensorValue);
        outMsg.setPayload(outPayload);

        return outMsg;
    }

}
