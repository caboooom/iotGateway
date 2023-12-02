package com.nhnacademy.aiot.node;

import java.util.Queue;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import com.nhnacademy.aiot.Msg;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttInNode extends Node {

    private Queue<Msg> innerMsgQueue;
    private ClientNode clientNode;
    private String topicFilter;
    


    public MqttInNode(String id, String topicFilter) {
        super(id, false, 1);
        this.topicFilter = topicFilter;
    }

    public MqttInNode(JsonNode jsonNode){
       // TODO flows.json에 토픽필터 추가
        this(jsonNode.path("id").asText(), "topicFilter");
    }

    @Override
    public void preprocess() {
        log.info("start node : " + name );
        try {
            clientNode.connect();
            clientNode.start();
            clientNode.subscribe(topicFilter);
        } catch (MqttSecurityException e) {
           log.error("Client Node subscribe() Error -" + e.getMessage());
        } catch (MqttException e) {
            log.error("Client Node subscribe() Error -" + e.getMessage());
        }
       
    }

    @Override
    public void process() {
        if (!innerMsgQueue.isEmpty()) {
            Msg msg = innerMsgQueue.poll();
            out(msg);
        }
    }

    @Override
    public void postprocess() {
        try {
            clientNode.disconnect();
        } catch (MqttException e) {
            log.error("disconnect Error - " + e.getMessage());
        }
    }

    public void setClientNode(ClientNode clientNode) {
        this.clientNode = clientNode;
        this.innerMsgQueue = clientNode.getClientToMqttQueue();
    }

}
