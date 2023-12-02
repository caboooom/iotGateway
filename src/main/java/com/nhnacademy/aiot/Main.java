package com.nhnacademy.aiot;

import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.FilterNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.SensorTypeFilterNode;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.util.Config;


public class Main {

    public static void main(String[] args) {

        Config command = new Config(args);
        command.set();
        MqttInNode mqttInNode = new MqttInNode(1, "tcp://ems.nhnacademy.com", "cla");
        MqttOutNode mqttOutNode = new MqttOutNode(1);
        Node debugNode = new DebugNode();
        
        DebugNode filterDebuger = new DebugNode();

        FilterNode filterNode2 = new FilterNode(1, 1 , new String[]{"place", "devEui", "object","branch"});
        SplitNode splitNode = new SplitNode(1, 1, "object");

        Wire wire = new Wire();
        Wire wire2 = new Wire();

        //split Node
        Wire wire4 = new Wire();

        //filter Wire;
        Wire wire5 = new Wire();
        Wire wire6 = new Wire();

        //filterToSplitWire
        mqttInNode.setOutputWire(0, wire5);
        filterNode2.setInputWire(0, wire5);
        filterNode2.setOutputWire(0, wire6);
        filterNode2.setOutputWire(0, wire);
        filterDebuger.setInputWire(0, wire6);

        splitNode.setInputWire(0, wire);
        splitNode.setOutputWire(0, wire4);
        debugNode.setInputWire(0,  wire4);
        //debugNode.setInputWire(0,  wire1);

        mqttOutNode.setInputWire(0, wire2);

        mqttInNode.start();
        mqttOutNode.start();
        debugNode.start();
        splitNode.start();
        filterNode2.start();
        filterDebuger.start();
    }
}
