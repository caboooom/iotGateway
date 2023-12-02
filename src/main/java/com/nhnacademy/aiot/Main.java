package com.nhnacademy.aiot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.FilterNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.util.Config;
import com.nhnacademy.aiot.util.JSONUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {


    static Map<String, Object> nodeMap;

    public static void main(String[] args) {


        nodeMap = new HashMap<>();

        Config command = new Config(args);
        command.set();

        
        NodeGenerator nodeGenerator = new NodeGenerator();
        nodeGenerator.generateNodes();
        


        

        // wire 연결
        



        // MqttInNode mqttInNode = new MqttInNode(1, "tcp://ems.nhnacademy.com", "cla");
        // MqttOutNode mqttOutNode = new MqttOutNode( "tcp://localhost:1883");

        // DebugNode spliterDebugNode = new DebugNode();
        // DebugNode filterDebuger = new DebugNode();

        // FilterNode filterNode = new FilterNode( 1 , new String[]{"place", "devEui", "object","branch"});
        // SplitNode splitNode = new SplitNode( 1, "object");

        // Wire wire = new Wire();
        // Wire wire1 = new Wire();
        // Wire wire2 = new Wire();
        // Wire wire3 = new Wire();
        // Wire wire4 = new Wire();
      
        // mqttInNode.setOutputWire(0, wire);
        // filterNode.setInputWire( wire);

        // filterNode.setOutputWire(0, wire2);
        // filterDebuger.setInputWire( wire2);

        // filterNode.setOutputWire(0, wire1);
        // splitNode.setInputWire(wire1);

        // splitNode.setOutputWire(0, wire3);
        // spliterDebugNode.setInputWire(wire3);
        
        // splitNode.setOutputWire(0, wire4);
        // mqttOutNode.setInputWire(wire4);

        // mqttInNode.start();
        // //mqttOutNode.start();
        // filterNode.start();
        // splitNode.start();
        // spliterDebugNode.start();
        // filterDebuger.start();
    }
}
