package com.nhnacademy.aiot.node;

import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;

public class SwitchNode extends Node {
    
    private static final String NODE_ID = "id";
    private static final String WIRES ="wires";
    private static final String TARGET_KEY_SET = "targetKeySet";

    private String[] targetKeySet;

    public SwitchNode(String id, int outputPortCount, String[] targetKeySet){
        super(id, outputPortCount);
        this.targetKeySet = targetKeySet;
    }

    public SwitchNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText(), jsonNode.path(WIRES).size(),
            StreamSupport.stream(jsonNode.path(TARGET_KEY_SET).spliterator(), false)
                    .map(JsonNode::asText)
                    .toArray(String[]::new));
    }

    @Override
    public void process() {
        if(!inputPort.hasMessage()) return;

        Msg msg = inputPort.getMsg();
        
        if(isValid(msg.getPayload())){
            out(msg);
        }

    }

    private boolean isValid(JsonNode payload){

        for(String key : targetKeySet){
            if (payload.path(key).isMissingNode()) return false;
        }

        return true;
    }
}
