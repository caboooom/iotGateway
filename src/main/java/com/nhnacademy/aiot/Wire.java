package com.nhnacademy.aiot;

import java.util.LinkedList;
import java.util.Queue;

public class Wire {
    Queue<Msg> msgQueue;

    public Wire(){
        msgQueue = new LinkedList<>();
    }

    public void put(Msg msg) {
        msgQueue.offer(msg);
    }

    public boolean hasMessage() { 
        return !msgQueue.isEmpty();
    }

    public Msg get() {
        return msgQueue.poll();
    }

}
