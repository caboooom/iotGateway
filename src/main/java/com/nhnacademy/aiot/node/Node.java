package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Wire;

public class Node implements Runnable{
    Thread thread;
    Wire[] inputWires;
    Wire[] outputWires;
    int inCount = 0;
    int outCount = 0;
    int errCount = 0;
    
    protected Node(int inputWireCount, int outputWireCount){

        this.inputWires = new Wire[inputWireCount];
        this.outputWires = new Wire[outputWireCount];
    }

    public void preprocess(){

    }

    public void process(){

    }

    public void postprocess(){
    }

    public void out(Msg outMessage){
       
        if(outMessage != null){
            outCount++;
            for (Wire wire : outputWires) {
                wire.put(outMessage);
            }
        }
        errCount++;
    }

    public void setInputWire(int wireIdx, Wire inpuWire) {
        this.inputWires[wireIdx] = inpuWire;
    }

    public void setOutputWire(int wireIdx, Wire outputWire) {
        this.outputWires[wireIdx] = outputWire;
    }


    public synchronized void start() {
        thread = new Thread(this, this.getClass().getSimpleName());
        thread.start();
    }

    @Override
    public void run() {
        preprocess();

        while ( (thread != null) && thread.isAlive()) {
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

