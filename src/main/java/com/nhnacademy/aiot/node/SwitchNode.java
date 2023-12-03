package com.nhnacademy.aiot.node;

import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;

public class SwitchNode extends Node {
    
    private static final String NODE_ID = "id";
    private static final String WIRES ="wires";
    private static final String TARGET_KEYS = "targetKeys";

    private String[] targetKeys;

    public SwitchNode(String id, int outputPortCount, String[] targetKeys){
        super(id, outputPortCount);
        this.targetKeys = targetKeys;
    }

    public SwitchNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText(), jsonNode.path(WIRES).size(),
            StreamSupport.stream(jsonNode.path(TARGET_KEYS).spliterator(), false)
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

        for(String key : targetKeys){
            if (payload.path(key) == null) return false;
        }
        return true;
    }
}
