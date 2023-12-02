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


    

    public static void main(String[] args) {


        Config command = new Config(args);
        command.set();

        
        FlowGenerator flowGenerator = new FlowGenerator();
        flowGenerator.generateNodes();

        flowGenerator.generateOutWires();
        flowGenerator.connectWires();

        flowGenerator.start();

    }
}
