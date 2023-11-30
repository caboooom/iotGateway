package com.nhnacademy.aiot.test;

import org.apache.logging.log4j.message.Message;
import org.json.simple.JSONObject;

interface messageManage {

    abstract void setTopic();
    abstract void setPayload();

    void setTopic(String topic);

    void setPayload(JSONObject payload);

    abstract JSONObject getPayload();
    abstract String getTopic();
    abstract Message createMsg();

}
