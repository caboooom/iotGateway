package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.Port;
import com.nhnacademy.aiot.Wire;
import lombok.extern.log4j.Log4j2;

/**
 * Node 클래스
 *
 */
@Log4j2
public class Node implements Runnable {
    private Thread thread;
    protected Port inputPort; // inputPort
    protected Port[] outputPorts; // Port 형태의 배열, input 은 한정되어 있지만 output 은 여러 PORT로 나가는 잠재성이 있음
    protected static int nodeCount; // node 개수
    protected String name;

    protected int inCount = 0;
    protected int outCount = 0;
    protected int errCount = 0;

    /**
     * Node 생성자
     *
     * @param outputPortCount output 하는 포트 개수
     */
    protected Node(int outputPortCount) {

        this.inputPort = new Port();
        this.outputPorts = new Port[outputPortCount];
        name = getClass().getSimpleName() + "_" + nodeCount++;
        log.info("create node : " + name);

        for (int i = 0; i < outputPortCount; i++) {
            outputPorts[i] = new Port();
        }
    }

    public void preprocess() {
        log.info("start node : " + name);
    }

    /**
     * extend 받는 하위 클래스에서 구현
     */
    public void process() {}

    public void postprocess() {
        log.info(this.getClass().getSimpleName() + " - stop");
    }

    /**
     * MESSAGE 하나 OUT
     *
     * @param outMessage MESSAGE 하나
     */
    public void out(Msg outMessage) {
        outputPorts[0].out(outMessage);
    }

    /**
     * MESSAGE 여러개 OUT
     *
     * @param outMessages MESSAGE 여러개
     */
    public void out(Msg... outMessages) {

        for (int i = 0; i < outMessages.length; i++) {
            outputPorts[i].out(outMessages[i]);
        }
    }

    /**
     * inputWire 설정
     *
     * @param inputWire inputWire
     */
    public void setInputWire(Wire inputWire) {
        inputPort.addWire(inputWire);
    }

    /**
     * outputWire 설정
     *
     * @param portIdx PORT_INDEX
     * @param outputWire OUTPUT_WIRE
     */
    public void setOutputWire(int portIdx, Wire outputWire) {
        if (portIdx < outputPorts.length) {
            outputPorts[portIdx].addWire(outputWire);
        }
    }

    public synchronized void start() {
        thread = new Thread(this, this.getClass().getSimpleName());
        thread.start();
    }

    /**
     *
     */
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
