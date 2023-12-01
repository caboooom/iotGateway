package com.nhnacademy.aiot;

import com.fasterxml.jackson.databind.JsonNode;

public class Msg {

    private String topic;
    private JsonNode payload;

    public Msg(String topic, JsonNode payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public Msg(){
        
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

    @Override
    public String toString() {
        
        return "{" + "topic : \"" + topic +"\", "
                   + "payload :" + payload + "}";
    }
}   
