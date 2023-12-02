package com.nhnacademy.aiot.node;

import java.util.Queue;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttOutNode extends Node {

    private Queue<Msg> innerMsgQueue;
    private ClientNode clientNode;

    public MqttOutNode(String id, int outputWireCount) {
        super(id,outputWireCount);
    }

    
    public MqttOutNode(String id) {
        super(id, 0);
        
    }

    public MqttOutNode(JsonNode jsonNode){
        this(jsonNode.path("id").asText(), jsonNode.path("wires").size());
    }

    



    @Override
    public void preprocess() {
        log.info("start node : " + name);
        try {
            clientNode.connect();
            clientNode.start();
        } catch (MqttException e) {
            log.error(name + "- preprocess() Error");
        }
        
    }

    @Override
    public void process() {

        if (inputPort.hasMessage()) {
            innerMsgQueue.add(inputPort.getMsg());
        }
    }

    public void setClientNode(ClientNode clientNode) {
        this.clientNode = clientNode;
        this.innerMsgQueue = clientNode.getMqttToClientQueue();
    }

    @Override
    public void postprocess() {
        try {
            clientNode.disconnect();
        } catch (MqttException e) {
            log.error("disconnect Error" + e.getMessage());
        }
    }

}
