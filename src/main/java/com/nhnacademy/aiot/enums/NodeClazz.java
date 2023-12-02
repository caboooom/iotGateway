package com.nhnacademy.aiot.enums;

import com.nhnacademy.aiot.node.DebugNode;
import com.nhnacademy.aiot.node.FilterNode;
import com.nhnacademy.aiot.node.Node;

public enum NodeClazz {

    FILTER_NODE("filterNode", FilterNode.class),
    SPLIT_NODE("splitNode",Node.class),
    DEBUG_NODE("debugNode", DebugNode.class);

    final String name;
    final Class<?> clazz;

    NodeClazz(String name, Class<?> class1) {

        this.name = name;
        this.clazz = class1;
    }

    public String getName() {
        return name;
    }

    public static Class<?> searchClass(String name) {
        for (NodeClazz node : NodeClazz.values()) {
            if (node.name.equals(name)) {
                return node.clazz;
            }
        }
        return null;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
