package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Port;
import com.nhnacademy.aiot.Wire;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Node implements Runnable {

    private Thread thread;
    protected Port inputPort;
    protected Port[] outputPorts;
    protected static int nodeCount;
    protected String name;

    protected int inCount = 0;
    protected int outCount = 0;
    protected int errCount = 0;

    protected final String id;

   
    protected Node(String id, boolean hasInputPort, int outputPortCount) {
        this.id = id;
        if (hasInputPort) {
            this.inputPort = new Port();
        }

        this.outputPorts = new Port[outputPortCount];
        name = getClass().getSimpleName() + "_" + nodeCount++;
        log.info("create node : " + name);

        for (int i = 0; i < outputPortCount; i++) {
            outputPorts[i] = new Port();
        }
    }

    protected Node(String id, int outputPortCount) {
        this(id, true, outputPortCount);
    }

    public void preprocess() {
        log.info("start node : " + name);
    }

    public void process() {
        // 이 메서드는 상속받는 하위 클래스에서 구현한다.
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

    public void setInputWire(Wire inputWire) {
        inputPort.addWire(inputWire);
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
