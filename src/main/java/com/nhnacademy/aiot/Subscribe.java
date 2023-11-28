package com.nhnacademy.aiot;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.*;
public class Subscribe implements MqttCallback {

        public void run(){
        try{
            
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            
            String clientId = UUID.randomUUID().toString();
            MqttClient subscriber = new MqttClient("tcp://ems.nhnacademy.com:1883", clientId);
            
            subscriber.setCallback(this);
            subscriber.connect(options);

            subscriber.subscribe("#", 1);
            System.out.println("Connected");
        } catch(MqttException e){

        }
    }


    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectionLost'");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        System.out.println("msg: " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deliveryComplete'");
    }
public static void main(String[] args) {
    new Subscribe().run();
}
}
