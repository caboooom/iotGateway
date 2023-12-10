package com.nhnacademy.aiot;

import com.nhnacademy.aiot.modbus.ModbusServerNode;
import com.nhnacademy.aiot.node.DebugNode;

public class Main {

    public static void main(String[] args) {
        
        byte a = -128;
        int b = a;
        System.out.println(Integer.toBinaryString(b));

        Wire wire = new Wire();
        DebugNode debugNode =  new DebugNode("sd");
        ModbusServerNode modbusServerNode = new ModbusServerNode("123",1);
        modbusServerNode.setOutputWire(0, wire);
        debugNode.setInputWire(wire);

        modbusServerNode.start();
        debugNode.start();
       // new FlowGenerator().start();
    }
}
