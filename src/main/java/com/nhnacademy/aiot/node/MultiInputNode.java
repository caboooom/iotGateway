package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Port;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MultiInputNode extends Node{
    
    public MultiInputNode(){
        super(3 , 3);
    }


    @Override
    public void process() {

        for(Port inputPort : inputPorts ){
           
            if(!inputPort.hasMessage()){
                inputPort.collectMsgFromWire();
                continue;
            }

            log.info(inputPort.getMsg());

    }
}
}
