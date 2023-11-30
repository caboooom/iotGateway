package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Port;
import com.nhnacademy.aiot.Wire;
import lombok.extern.log4j.Log4j2;

<<<<<<< HEAD
public class Node implements Runnable{
    Thread thread;
    Wire[] inputWires;
    Wire[] outputWires;
    int inCount = 0;
    int outCount = 0;
    int errCount = 0;
    
    protected Node(int inputWireCount, int outputWireCount){
=======
@Log4j2
public class Node implements Runnable {
    private Thread thread;
    protected Port[] inputPorts;
    protected Port[] outputPorts;
>>>>>>> origin/develop

    protected int inCount = 0;
    protected int outCount = 0;
    protected int errCount = 0;

<<<<<<< HEAD
    public void preprocess(){

    }
=======
    protected Node(int inputPortCount, int outputPortCount) {
        this.inputPorts = new Port[inputPortCount];
        this.outputPorts = new Port[outputPortCount];
>>>>>>> origin/develop

        for (int i = 0; i < inputPortCount; i++) {
            inputPorts[i] = new Port();
        }

<<<<<<< HEAD
    }

    public void postprocess(){
    }

    public void out(Msg outMessage){
       
        if(outMessage != null){
            outCount++;
            for (Wire wire : outputWires) {
                wire.put(outMessage);
            }
=======
        for (int i = 0; i < outputPortCount; i++) {
            outputPorts[i] = new Port();
>>>>>>> origin/develop
        }
        errCount++;
    }

    public void preprocess() {
        log.info(this.getClass().getSimpleName() + " - start");
    }

    public void process() {

    }

    public void postprocess() {
        log.info(this.getClass().getSimpleName() + " - stop");
    }

    public void out(Msg outMessage) {
        
        outputPorts[0].out(outMessage);
            
    }

    public void out(Msg... outMessages) {
        
        for (int i = 0; i < outMessages.length; i++) {
            outputPorts[i].out(outMessages[i]);
        }
    }

    public void setInputWire(int portIdx, Wire inputWire) {
        if (portIdx < inputPorts.length) {
            inputPorts[portIdx].addWire(inputWire);
        }
    }

    public void setOutputWire(int portIdx, Wire outputWire) {
        if (portIdx < outputPorts.length) {
            outputPorts[portIdx].addWire(outputWire);
        }
    }

    public synchronized void start() {
        thread = new Thread(this, this.getClass().getSimpleName());
        thread.start();
    }

    @Override
    public void run() {
        preprocess();

<<<<<<< HEAD
        while ( (thread != null) && thread.isAlive()) {
=======
        while ((thread != null) && thread.isAlive()) {
            process();
>>>>>>> origin/develop
            try {
                process();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                if (thread != null) {
                    thread.interrupt();
                }
                errCount++;
            }
        }

        postprocess();
    }
}