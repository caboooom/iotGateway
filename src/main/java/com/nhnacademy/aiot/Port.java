package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Port {
    private List<Wire> wires;
    private Queue<Msg> messageQueue;

    public Port() {
        this.wires = new ArrayList<>();
        this.messageQueue = new LinkedList<>();
    }

    public void addWire(Wire wire) {
        wires.add(wire);
    }

    public void removeWire(Wire wire) {
        wires.remove(wire);
    }

    public List<Wire> getWires() {
        return wires;
    }

    public void out(Msg outMessage) {
        if (outMessage != null) {
            for (Wire wire : wires) {
                wire.put(outMessage);
            }
        }
    }

    public boolean hasMessage(){
        return !messageQueue.isEmpty();
    }
    /**
     * 연결된 여러 wire 에서 메시지를 하나씩 가져와서 Port의 메시지 큐에 추가하여.
     * wire로부터 오는 메시지를 큐에 모아주는 메서드.
     */
    public void collectMsgFromWire(){
        for (Wire wire : wires) {
            if(wire.hasMessage()){
                messageQueue.add(wire.get());
            }
        }
    }

    public Msg getMsg(){
        
        return messageQueue.poll();
    }
}