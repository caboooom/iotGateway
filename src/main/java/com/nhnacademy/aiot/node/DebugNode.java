package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DebugNode extends Node{

    public DebugNode(String id) {
        super(id, 0);
    }

    public DebugNode(JsonNode jsonNode){
        this(jsonNode.path("id").asText());
    }
    @Override
    public void process() {
        if(inputPort.hasMessage()){
            log.debug(getClass().getSimpleName() + " " + inputPort.getMsg());
        }
    }

}
