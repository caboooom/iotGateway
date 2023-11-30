package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Wire;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DebugNode extends Node{

    public DebugNode() {
        super(1,0);
    }

    @Override
    public void process() {
        for (Wire wire : inputPorts[0].getWires()) {
            if(wire.hasMessage()){
                log.debug(getClass().getSimpleName() + " " + wire.get());
            }
        }
    }


}
