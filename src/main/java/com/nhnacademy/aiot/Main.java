package com.nhnacademy.aiot;

import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.Node;


public class Main {

    public static void main(String[] args) {
        MqttInNode mqttInNode = new MqttInNode(2);
        MqttOutNode mqttOutNode = new MqttOutNode(1);
        Node debugNode = new DebugNode();


        Wire wire = new Wire();
        Wire wire1 = new Wire();

        mqttInNode.setOutputWire(0, wire);
        mqttInNode.setOutputWire(1, wire1);
        mqttOutNode.setInputWire(0,wire);
        debugNode.setInputWire(0, wire1);

        mqttInNode.start();
        mqttOutNode.start();
        debugNode.start();
    }

}
