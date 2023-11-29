package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Port;
import com.nhnacademy.aiot.Wire;

public class Node implements Runnable {
    private Thread thread;
    protected Port[] inputPorts;
    protected Port[] outputPorts;

    protected int inCount = 0;
    protected int outCount = 0;
    protected int errCount = 0;

    protected Node(int inputPortCount, int outputPortCount) {
        this.inputPorts = new Port[inputPortCount];
        this.outputPorts = new Port[outputPortCount];

        for (int i = 0; i < inputPortCount; i++) {
            inputPorts[i] = new Port();
        }

        for (int i = 0; i < outputPortCount; i++) {
            outputPorts[i] = new Port();
        }
    }

    public void preprocess() {

    }

    public void process() {

    }

    public void postprocess() {

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

        while ((thread != null) && thread.isAlive()) {
            process();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                if (thread != null) {
                    thread.interrupt();
                }
            }
        }

        postprocess();
    }
}