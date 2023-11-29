package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.List;

public class Port {
    private List<Wire> wires;

    public Port() {
        this.wires = new ArrayList<>();
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
}