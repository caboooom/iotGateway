package com.nhnacademy.aiot.node;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DebugNode extends Node{

    public DebugNode() {
        super(0);
    }

    @Override
    public void process() {
        if(inputPort.hasMessage()){
            log.debug(getClass().getSimpleName() + " " + inputPort.getMsg());
        }
    }

}
