package com.nhnacademy.aiot.node;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DebugNode extends Node{

    public DebugNode() {
        super(1,0);
    }
    

    @Override
    public void process() {
        if(inputWires[0].hasMessage()){
            log.info(inputWires[0].get());
        }
    }


}
