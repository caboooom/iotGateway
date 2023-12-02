package com.nhnacademy.aiot.node;

import java.util.LinkedList;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttInNode extends Node {

    private String topic;
    private String serverURI;
    private String clientId;
    private Queue<MqttMessage> innerMsgQueue;
    

    public MqttInNode(String id, int outputWireCount) {
        super(id, 0, outputWireCount);
        this.topic = "application/+/device/+/+/up";
        innerMsgQueue = new LinkedList<>();
    }


    public MqttInNode(JsonNode jsonNode){
       
        this(jsonNode.path("id").asText(), jsonNode.path("wires").size());

    }


    @Override
    public void preprocess() {
        log.info("start node : " + name );
        
        ClientNode node = new ClientNode();
        node.run();
    }

    @Override
    public void process() {
        if (!innerMsgQueue.isEmpty()) {
            MqttMessage mqttMessage = innerMsgQueue.poll();
            String payload = new String(mqttMessage.getPayload());
            Msg msg = createMsg(topic, payload);
            
            out(msg);
        }
    }

    private Msg createMsg(String topic, String payload) {
        if (JSONUtils.isJson(payload)) {
                ObjectNode jsonObject = (ObjectNode) JSONUtils.parseJson(payload);
                jsonObject.put("time", System.currentTimeMillis());
                return new Msg(topic, jsonObject);
            }
            return null;
        }

    public class ClientNode extends Node {
        MqttClient client;
        protected ClientNode() {
            super("id", 0, 0);
        }


        @Override
        public void run() {
            log.info(name + " : start");

            try {
                client = new MqttClient(serverURI, clientId);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setConnectionTimeout(10);
                client.connect();
                client.subscribe(topic, (topic, msg) -> innerMsgQueue.add(msg));
                
            } catch (MqttException e) {
                log.error("ClientNode run() "+ e.getMessage());
            }
        }

    }



}
