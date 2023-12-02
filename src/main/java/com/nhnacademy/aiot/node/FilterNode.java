package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class FilterNode extends Node {

    private Set<String> targetStrings;

    public FilterNode(int inputPortCount, int outputPortCount, String[] targetStrings) {
        super(inputPortCount, outputPortCount);
        this.targetStrings = new HashSet<>();
        for (String targetString : targetStrings) {
            this.targetStrings.add(targetString);
        }
    }

    @Override
    public void process() {
        if (!inputPorts[0].hasMessage()) {
            return;
        }

        Msg inMsg = inputPorts[0].getMsg();
        JsonNode inPayload = inMsg.getPayload();

        ObjectNode outPayload = createFilteredPayload(inPayload);

        Msg outMsg = new Msg("", outPayload);
        out(outMsg);
    }

    private ObjectNode createFilteredPayload(JsonNode inPayload) {
        ObjectNode outPayload = JSONUtils.getMapper().createObjectNode();

        filterJsonNode(inPayload, outPayload);

        return outPayload;
    }

    private void filterJsonNode(JsonNode inPayload, ObjectNode outPayload) {
        Iterator<Entry<String, JsonNode>> entryIterator = inPayload.fields();
        while (entryIterator.hasNext()) {
            Entry<String, JsonNode> entry = entryIterator.next();

            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            if (targetStrings.contains(fieldName)) {
                outPayload.set(fieldName, fieldValue);
            }
            if (fieldValue.isObject()) {
                filterJsonNode(fieldValue, outPayload);
            }

        }
    }
}
