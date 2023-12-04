package com.nhnacademy.aiot.enums;

import com.nhnacademy.aiot.node.Node;
import lombok.Getter;

@Getter
public enum Nodes {
    
    FUNCTION_NODE("functionNode", Node.class),
    MQTT_IN_NODE("mqttInNode", Node.class),
    MQTT_OUT_NODE("mqttOutNode", Node.class),
    CLIENT_NODE("clientNode", Node.class);

    private final String nodeType;
    private final Class<?> clazz;

    Nodes(String nodeType, Class<?> clazz) {
        this.nodeType = nodeType;
        this.clazz = clazz;
    }

    public static Class<?> getNodeClass(String nodeType) {
        for (Nodes node : Nodes.values()) {
            if (node.getNodeType().equals(nodeType)) {
                return node.getClazz();
            }
        }
        return null;
    }

}