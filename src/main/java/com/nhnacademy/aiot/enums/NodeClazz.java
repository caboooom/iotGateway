package com.nhnacademy.aiot.enums;

import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.FilterNode;
import com.nhnacademy.aiot.node.MqttInNode;

public enum NodeClazz {
    
    

    MQTT_IN_NODE("mqttInNode", MqttInNode.class),
    FILTER_NODE("filterNode", FilterNode.class),
    DEBUG_NODE("debugNode", DebugNode.class);

    String name;
    Class<?> clazz;

    NodeClazz(String name, Class<?> class1){
        this.name = name;
        this.clazz = class1;
    }

    public String getName(){
        return name;
    }

    public Class<?> getClazz(){
        return clazz;
    }

    public static Class<?> searhClass(String name){
        for(NodeClazz node : NodeClazz.values()){
            if (node.getName().equals(name)){
                return node.getClazz();
            }
        }
    }

}
