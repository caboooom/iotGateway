package com.nhnacademy.aiot;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 사용자 정의 Port 클래스
 * Wire Type 리스트를 가지고 있음
 * Msg Type Queue를 가지고 있음
 */
public class Port {
    @Getter
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

    public void out(Msg outMessage) {
        if (outMessage != null) {
            for (Wire wire : wires) {
                wire.put(outMessage);
            }
        }
    }

    public boolean hasMessage(){

        if(messageQueue.isEmpty()){
            collectMsgFromWire();
            return false;
        }
        return true;
    }

    /**
     * 연결된 여러 wire에서 메세지를 하나씩 가져와 Port안의 메시지 큐에 추가
     * wire에서 오는 다수의 메세지를 큐에 담는 메서드.
     */
    private void collectMsgFromWire(){
        for (Wire wire : wires) {
            if (wire.hasMessage()) {
                messageQueue.add(wire.get());
            }
        }
    }

    public Msg getMsg() {

        return messageQueue.poll();
    }
}
