package com.nhnacademy.aiot.node;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.nhnacademy.aiot.Msg;

public class MqttOutNode extends Node{

    public MqttOutNode(int inputWireCount) {
        super(inputWireCount, 0);
    }

    @Override
    public void process() {

       
        if(inputWires[0].hasMessage()){
            inCount++;
            Msg msg = inputWires[0].get();
            String publisherId = UUID.randomUUID().toString();

        try (IMqttClient client = new MqttClient("tcp://localhost:1883", publisherId)) {
            MqttConnectOptions options = new MqttConnectOptions();

            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, false);

            client.connect();
            MqttMessage mqttMessage = new MqttMessage(msg.getPayload().toString().getBytes());
            client.publish( msg.getTopic(), mqttMessage );
            client.disconnect();
            
        } catch (MqttException e) {
            e.printStackTrace();
        }
        }

        
    }
        
    }
    

