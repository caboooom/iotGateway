package com.nhnacademy.aiot.node;

import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SplitNode extends Node {

    private static final String NODE_ID = "id";
    private static final String WIRES = "wires";
    private static final String SPLIT_KEY = "splitKey";
    private static final String KEY_HOLDER = "keyHolder";
    private static final String DEFAULT_KEY_HOLDER = "";
    
    private String keyHolder;
    private String splitKey;
    //keep Msg 구현해야됨
    //private boolean keepMsg;

    public SplitNode(String id, int outputPortCount, String splitKey, String keyHolder) {
        super(id, outputPortCount);
        this.splitKey = splitKey;
        this.keyHolder = keyHolder;
       
    }

    public SplitNode(String id, int outputPortCount, String splitKey) {
        this( id, outputPortCount, splitKey , DEFAULT_KEY_HOLDER);
    }

    public SplitNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText(), jsonNode.path(WIRES).size(), jsonNode.path(SPLIT_KEY).asText() , jsonNode.path(KEY_HOLDER).asText());
    }

    @Override
    public void process() {
        
        if(!inputPort.hasMessage()) return;
            
        JsonNode inputMsg = inputPort.getMsg().getPayload();
        Msg msg = spliter(inputMsg, splitKey);
        if (msg != null){
            out(msg);
        }
        
    }

    private Msg spliter(JsonNode payload, String key) {
        try {
            if (!payload.has(key)) {
                return null;
            }
            JsonNode objectNode = payload.get(key);

            if (objectNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = objectNode.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fieldsIterator.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    return createMsg(payload, key, fieldName, fieldValue);
                }
            }

        } catch (Exception e) {
           log.error(e.getMessage());
        }

        return null;
    }

    private Msg createMsg(JsonNode payload, String key, String fieldName, JsonNode fieldValue) {
        ObjectNode jsonNode = (ObjectNode) payload.deepCopy();
        
        jsonNode.remove(key);
        jsonNode.put(keyHolder , fieldName);
        jsonNode.set(fieldName, fieldValue);

        return new Msg("", jsonNode);
    }
}
