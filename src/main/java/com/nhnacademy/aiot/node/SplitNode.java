package com.nhnacademy.aiot.node;

import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SplitNode extends Node {

    private String splitKey;
    //keep Msg 구현해야됨
    private boolean keepMsg;

    public SplitNode(int inputPortCount, int outputPortCount, String splitKey, boolean keepMsg) {
        super(inputPortCount, outputPortCount);
        this.splitKey = splitKey;
        this.keepMsg = keepMsg;
    }

    public SplitNode(int inputPortCount, int outputPortCount, String splitKey) {
        this(inputPortCount, outputPortCount, splitKey, true);
    }

    @Override
    public void process() {

        if (inputPorts[0].hasMessage()) {
            JsonNode inputMsg = inputPorts[0].getMsg().getPayload();
            spliter(inputMsg, splitKey);
        }

    }

    private void spliter(JsonNode payload, String key) {
        try {
            if (!payload.has(key)) {
                return;
            }
            JsonNode objectNode = payload.get(key);

            if (objectNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = objectNode.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fieldsIterator.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    Msg newMsg = createMsg(payload, key, fieldName, fieldValue);

                    out(newMsg);
                }
            }

        } catch (Exception e) {
           log.error(e.getMessage());
        }
    }

    private Msg createMsg(JsonNode payload, String key, String fieldName, JsonNode fieldValue) {
        ObjectNode jsonNode = (ObjectNode) payload.deepCopy();
        
        jsonNode.remove(key);
        jsonNode.set(fieldName, fieldValue);

        return new Msg("", jsonNode);
    }
}
