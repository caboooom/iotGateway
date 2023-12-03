package com.nhnacademy.aiot.node;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;

/**
 *  MQTT 에서 받는 클래스
 */
@Log4j2
public class MqttInNode extends Node {
    private String topic;
    private String serverURI;
    private String clientId;
    private Queue<MqttMessage> innerMsgQueue;

    /**
     * MqttInNode 생성자
     *
     * @param outputWireCount outputWire 개수
     * @param serverURI serverURI
     * @param clientId client 식별자
     */
    public MqttInNode(int outputWireCount, String serverURI, String clientId) {
        super(outputWireCount);
        this.topic = "application/+/device/+/+/up";
        this.serverURI = serverURI;
        this.clientId = clientId;
        innerMsgQueue = new LinkedList<>();
    }

    /**
     * MqttInNode 생성자 -> outputWire 개수, serverURI 로 만들고 , 식별자ID 생성후 위 생성자 호출
     *
     * @param outputWireCount outputWire 개수
     * @param serverURI serverURI
     */
    public MqttInNode(int outputWireCount, String serverURI) {
        this(outputWireCount, serverURI, UUID.randomUUID().toString());
    }

    /**
     * 사전 수행 단계
     */
    @Override
    public void preprocess() {
        log.info("start node : " + name );
        
        ClientNode node = new ClientNode();
        node.run();
    }

    /**
     * 수행 단계
     * Queue 에 MESSAGE 가 있으면 createMSG Method 를 통해 계속 out(msg) 해줌
     */
    @Override
    public void process() {
        if (!innerMsgQueue.isEmpty()) {
            MqttMessage mqttMessage = innerMsgQueue.poll();
            String payload = new String(mqttMessage.getPayload());
            Msg msg = createMsg(topic, payload);
            
            out(msg);
        }
    }

    /**
     * 받은 데이터로 Msg 형태로 만들어줌 (시간을 추가로 넣어서)
     * @param topic TOPIC
     * @param payload 받은 데이터(String Type)
     * @return
     */
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
            super(0);
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
