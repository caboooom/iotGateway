package com.nhnacademy.aiot;

import java.util.HashMap;
import java.util.Map;

import com.nhnacademy.aiot.node.MqttInNode;

public class NodeGenerator {
    
    Map<String, Class<?>> nodeMap = new HashMap<>();

    NodeGenerator(){
        nodeMap.put("mqttInNode", MqttInNode.class);

    }


}
