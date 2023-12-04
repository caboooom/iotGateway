package com.nhnacademy.aiot.node;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.nhnacademy.aiot.Msg;

import lombok.extern.log4j.Log4j2;

/**
 *
 */
@Log4j2
public class MqttOutNode extends Node {

    private String serverURI;
    private String clientId;
    private Queue<Msg> innerMsgQueue;

    /**
     *
     * @param serverURI serverURI
     * @param clientId client 식별자
     */
    public MqttOutNode( String serverURI, String clientId) {
        super(0);
        this.serverURI = serverURI;
        this.clientId = clientId;
        innerMsgQueue = new LinkedList<>();
    }

    /**
     * MqttOutNode 생성자 -> serverURI가 주어지면 식별자ID 생성후 위 생성자 호출
     *
     * @param serverURI serverURI
     */
    public MqttOutNode(String serverURI) {
        this( serverURI, UUID.randomUUID().toString());
    }

    @Override
    public void preprocess() {
        log.info("start node : " + name);
        ClientNode clientNode = new ClientNode();
        clientNode.start();
    }

    @Override
    public void process() {

        if (inputPort.hasMessage()) {
            innerMsgQueue.add(inputPort.getMsg());
        }

    }

    public class ClientNode extends Node {
        MqttClient client;

        protected ClientNode() {
            super(0);
        }

        @Override
        public void preprocess() {
            try {
                client = new MqttClient(serverURI, clientId);

                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);

                client.connect();

            } catch (MqttException e) {
                log.error("Client preprocess()" + e.getMessage());
            }
        }

        @Override
        public void process() {
            if (!innerMsgQueue.isEmpty()) {
                Msg msg = innerMsgQueue.poll();
                MqttMessage mqttMessage = new MqttMessage(msg.getPayload().toString().getBytes());
                try {
                    client.publish(msg.getTopic(), mqttMessage);
                } catch (MqttPersistenceException e) {
                    log.error("Client process()" + e.getMessage());
                } catch (MqttException e) {
                    log.error("Client process()" + e.getMessage());
                }
            }
            super.process();
        }
    }
}
