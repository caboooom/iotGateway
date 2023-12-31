package com.nhnacademy.aiot;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.util.JSONUtils;

public class Msg {

    private String topic;
    private long createTime;
    private String msgId;
    private JsonNode payload;

        public Msg(String topic, JsonNode payload) {
        this.topic = topic;
        this.payload = payload;
        this.createTime = System.currentTimeMillis();
        this.msgId = UUID.randomUUID().toString();

        ObjectNode objectNode = (ObjectNode)payload;
        objectNode.put("time", createTime);
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public String getTopic() {
        return topic;
    }

    public JsonNode getJSON() {

        return JSONUtils.parseJson(this.toString());
    }

    @Override
    public String toString() {

        return "{" + "\"topic\" : \"" + topic + "\", " + 
                     "\"msgId\" : \"" + msgId + "\", " + 
                     "\"payload\" : " + payload + "}";

    }
}
