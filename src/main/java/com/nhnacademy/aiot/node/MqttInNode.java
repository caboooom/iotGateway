package com.nhnacademy.aiot.node;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.enums.CmdOptions;
import com.nhnacademy.aiot.util.Config;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttInNode extends Node {

    private String topic;
    private String serverURI;
    private String clientId;
    private Queue<MqttMessage> innerMsgQueue;

    public MqttInNode(int outputWireCount, String serverURI, String clientId) {
        super(0, outputWireCount);
        this.topic = "application/" + Config.getProperty(CmdOptions.APPLICATION_NAME.getKey())
                + "/device/+/+/up";
        this.serverURI = serverURI;
        this.clientId = clientId;
        innerMsgQueue = new LinkedList<>();
    }

    public MqttInNode(int outputWireCount, String serverURI) {
        this(outputWireCount, serverURI, UUID.randomUUID().toString());
    }


    @Override
    public void preprocess() {
        log.info("start node : " + name );
        
        ClientNode node = new ClientNode();
        node.start();
    }

    @Override
    public void process() {
        if (!innerMsgQueue.isEmpty()) {
            log.debug("asdasmvklsmrgikermgoiemrgoiermgo");
            MqttMessage mqttMessage = innerMsgQueue.poll();
            String payload = new String(mqttMessage.getPayload());
            Msg msg = createMsg(topic, payload);
            
            out(msg);
        }
    }

    private Msg createMsg(String topic, String payload) {
        log.debug(payload);
        if (JSONUtils.isJson(payload)) {
                JsonNode jsonObject = JSONUtils.parseJson(payload);
                
                return new Msg(topic, jsonObject);
            }
            return null;
        }
       
    

    public class ClientNode extends Node {

        protected ClientNode() {
            super(0, 0);
        }

        @Override
        public void run() {
            log.info(name + " : start");
            MqttClient client;

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
