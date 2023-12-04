package com.nhnacademy.aiot.node;

import java.util.LinkedList;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClientNode extends Node {
    private static final String NODE_ID = "id";
    private static final String CLIENT_ID = "clientId";
    private static final String SERVER_URI = "broker";

    private static final String PORT = "port";
    private MqttClient client;
    private Queue<Msg> clientToMqttQueue;
    private Queue<Msg> mqttToClientQueue;

    public ClientNode(String id, String serverURI, String clientId) {
        super(id, 0);
        clientToMqttQueue = new LinkedList<>();
        mqttToClientQueue = new LinkedList<>();
        try {
            client = new MqttClient(serverURI, clientId);
        } catch (MqttException e) {
            log.error("ClientNode Counstruct Error - " + e.getMessage());
        }
    }

    public ClientNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText(), 
        jsonNode.path(SERVER_URI).asText()+":"+jsonNode.path(PORT).asText(),
        jsonNode.path(CLIENT_ID).asText()
        );
    }

    @Override
    public void preprocess() {
        log.info(name + " : start");
    }

    @Override
    public void process() {

        if (!mqttToClientQueue.isEmpty()) {
            Msg msg = mqttToClientQueue.poll();
            MqttMessage mqttMessage = new MqttMessage(msg.getPayload().toString().getBytes());
            try {
                
                //msg.getTopic() topic 만들면 토픽 바꿔줘야함
                
                client.publish(msg.getTopic(), mqttMessage);
            } catch (MqttPersistenceException e) {
                log.error(e.getMessage());
            } catch (MqttException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * @param topicFilter
     * @throws MqttException
     * topicFiter 문자열을 받아서 생성한 client를 구독 후
     * 받은 MQTT메시지를 Msg객체로 만들어서 innerMsgQueue에 추가하는 메서드
     * 
     */
    public void subscribe(String topicFilter) throws MqttException {
        client.subscribe(topicFilter, (topic, mqttMsg) -> {
            String payload = new String(mqttMsg.getPayload());
            Msg msg = new Msg(topic, JSONUtils.parseJson(payload));
            clientToMqttQueue.add(msg);
        });
    }

    public void connect() throws MqttException {
        client.connect();
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public Queue<Msg> getClientToMqttQueue() {
        return clientToMqttQueue;
    }

    public Queue<Msg> getMqttToClientQueue() {
        return mqttToClientQueue;
    }

}
