package com.nhnacademy.aiot.node;

import org.json.simple.JSONObject;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Wire;
import com.nhnacademy.aiot.util.Config;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
        JSONObject payload = (JSONObject) msg.getPayload().get("object");

        if (payload == null) {
            return;
        }

        String deviceId = (String) ((JSONObject) msg.getPayload().get("deviceInfo")).get("devEui");

        for (String sensor : sensorTypes) {
            if (payload.get(sensor) != null) {
                Msg outMsg = createMessage(deviceId, sensor, (Double) payload.get(sensor));
                out(outMsg);
            }
        }
    }


    private Msg createMessage(String deviceId, String sensor, Double sensorValue) {
        Msg outMsg = new Msg();
        outMsg.setTopic(deviceId + "/d/" + sensor);

        JSONObject outPayload = new JSONObject();
        outPayload.put("time", System.currentTimeMillis());
        outPayload.put(sensor, sensorValue);
        return outMsg;
    }
}
