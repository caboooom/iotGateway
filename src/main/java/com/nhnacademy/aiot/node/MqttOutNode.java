package com.nhnacademy.aiot.node;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Port;
import com.nhnacademy.aiot.Wire;

public class MqttOutNode extends Node {


    public MqttOutNode(int inputWireCount) {
        super(inputWireCount, 0);
    }

    @Override
    public void process() {
        for (Port port : inputPorts) {
            for (Wire wire : port.getWires()) {
               pubMQTT(wire);
            }
        }
    }

    private void pubMQTT(Wire wire) {
        if (wire.hasMessage()) {
            Msg msg = wire.get();
            String publisherId = UUID.randomUUID().toString();

            try (IMqttClient client = new MqttClient("tcp://localhost:1883", publisherId)) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);
                options.setWill("test/will", "Disconnected".getBytes(), 1, false);

                client.connect();
                MqttMessage mqttMessage = new MqttMessage(msg.getPayload().toString().getBytes());
                client.publish(msg.getTopic(), mqttMessage);
                client.disconnect();

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


}


