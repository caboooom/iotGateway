//package com.nhnacademy.aiot.test;
//
//import org.json.simple.JSONObject;
//
//public class Message implements messageManage{
//    private String topic;
//    private JSONObject payload;
//
//    public Message(String topic, JSONObject payload){
//        this.topic = topic;
//        this.payload = payload;
//    }
//    public Message(){
//    }
//
//    @Override
//    public void setTopic(String topic) {
//        this.topic = topic;
//    }
//
//    @Override
//    public void setPayload(JSONObject payload) {
//        this.payload = payload;
//    }
//
//    @Override
//    public JSONObject getPayload() {
//        return payload;
//    }
//
//    @Override
//    public String getTopic() {
//        return topic;
//    }
//
//    @Override
//    public org.apache.logging.log4j.message.Message createMsg() {
//        return null;
//    }
//}
