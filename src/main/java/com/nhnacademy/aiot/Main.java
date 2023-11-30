package com.nhnacademy.aiot;

import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import com.nhnacademy.aiot.enums.Nodes;
import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.SensorTypeFilterNode;
import com.nhnacademy.aiot.util.Config;


public class Main {

    public static void main(String[] args) {


        Config command = new Config(args);
        command.set();
        MqttInNode mqttInNode = new MqttInNode(1, "tcp://ems.nhnacademy.com", "cla");
        MqttOutNode mqttOutNode = new MqttOutNode(1);
        SensorTypeFilterNode filterNode = new SensorTypeFilterNode(1, 1);
        Node debugNode = new DebugNode();
        
        Wire wire = new Wire();
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
      
        mqttInNode.setOutputWire(0, wire);
        filterNode.setInputWire(0, wire);

        filterNode.setOutputWire(0, wire1);
        filterNode.setOutputWire(0, wire2);

        debugNode.setInputWire(0,  wire1);

        mqttOutNode.setInputWire(0, wire2);

        mqttInNode.start();
        mqttOutNode.start();
        debugNode.start();
        filterNode.start();
    }
}
