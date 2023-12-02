package com.nhnacademy.aiot;

import com.nhnacademy.aiot.util.Config;

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
