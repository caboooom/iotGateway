package com.nhnacademy.aiot;

import org.json.simple.JSONObject;

public class Msg {

    private String topic;
    private JSONObject payload;

    public Msg(String topic, JSONObject payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public Msg(){
        
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }
    public JSONObject getPayload() {
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
