package com.nhnacademy.aiot;

import com.nhnacademy.aiot.node.*;
import com.nhnacademy.aiot.util.Config;


public class Main {

    public static void main(String[] args) {
        Config command = new Config(args);
        command.set();

        MqttInNode mqttInNode = new MqttInNode(2);
        MqttOutNode mqttOutNode = new MqttOutNode(1);
        SensorTypeFilterNode filterNode = new SensorTypeFilterNode(1, 1);
        FilterNode fn = new FilterNode(1, 1, args);
        Node debugNode = new DebugNode();

        Wire wire = new Wire();
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();

        mqttInNode.setOutputWire(0, wire);
//        filterNode.setInputWire(0, wire);
        fn.setInputWire(0, wire);

//        filterNode.setOutputWire(0, wire1);
//        filterNode.setOutputWire(0, wire2);
        fn.setOutputWire(0, wire1);
        fn.setOutputWire(0, wire2);



        debugNode.setInputWire(0,  wire1);

        mqttOutNode.setInputWire(0, wire2);

        mqttInNode.start();
        mqttOutNode.start();
        debugNode.start();
//        filterNode.start();
        fn.start();
       
    }

}
