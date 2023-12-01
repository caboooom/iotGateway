package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Wire;
import com.nhnacademy.aiot.util.Config;
import com.nhnacademy.aiot.util.JSONUtils;

public class SensorTypeFilterNode extends Node {

    String[] sensorTypes;

    public SensorTypeFilterNode(int inputPortCount, int outputPortCount) {
        super(inputPortCount, outputPortCount);

    }

    @Override
    public void preprocess() {
        sensorTypes = Config.getProperty("sensorTypes").split(",");
    }

    @Override
    public void process() {

        for (Wire wire : inputPorts[0].getWires()) {
            if (wire.hasMessage()) {
                Msg msg = wire.get();
                processMessage(msg);
            }
        }
    }

    private void processMessage(Msg msg) {
        JsonNode payload = msg.getPayload().get("object");

        if (payload == null) {
            return;
        }
        try {
            String deviceId = msg.getPayload().get("deviceInfo").get("devEui").asText();
            String place = msg.getPayload().get("deviceInfo").get("tags").get("place").asText();
            for (String sensor : sensorTypes) {
                if (payload.get(sensor) != null) {

                    Msg outMsg =
                            createMessage(deviceId, sensor, payload.get(sensor).asDouble(), place);
                    out(outMsg);
                }
            }
        } catch (Exception e) {
            return;
        }



    }


    private Msg createMessage(String deviceId, String sensor, Double sensorValue, String place) {
        Msg outMsg = new Msg();
        outMsg.setTopic("/d/" + deviceId + "/p/" + place + "/e/" + sensor);


        ObjectNode outPayload = JSONUtils.getMapper().createObjectNode();
        outPayload.put("time", System.currentTimeMillis());
        outPayload.put(sensor, sensorValue);
        outMsg.setPayload(outPayload);
        return outMsg;
    }
}
