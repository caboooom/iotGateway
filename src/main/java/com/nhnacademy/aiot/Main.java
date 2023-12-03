package com.nhnacademy.aiot;

import com.nhnacademy.aiot.util.Config;

public class Main {

    public static void main(String[] args) {

        Config command = new Config(args);
        command.set();
        
        FlowGenerator flowGenerator = new FlowGenerator();
        flowGenerator.generateNodes();
        flowGenerator.injectClients();

        flowGenerator.generateOutputWires();
        flowGenerator.connectWires();

        flowGenerator.start();

        //TODO generateTopicNode 와 디버그노드 연결 (flows.json)

    }
}
