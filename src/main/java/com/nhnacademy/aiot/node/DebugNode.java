package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DebugNode extends Node{

    private static final String NODE_ID = "id";

    public DebugNode(String id) {
        super(id, 0);
    }

    public DebugNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText());
    }

    @Override
    public void process() {
        if(inputPort.hasMessage()){
            log.debug(getClass().getSimpleName() + " " + inputPort.getMsg());
        }
    }

}
