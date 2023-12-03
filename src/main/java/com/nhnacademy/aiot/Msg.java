package com.nhnacademy.aiot;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.Getter;

/**
 * MESSAGE 를 사용자 정의한 클래스
 */
public class Msg {

    @Getter
    private String topic;
    private long createTime;
    private String msgId;
    @Getter
    private JsonNode payload;

    /**
     * Msg 의 생성자
     * @param topic TOPIC
     * @param payload 받은 데이터
     */
    public Msg(String topic, JsonNode payload) {
        this.topic = topic;
        this.payload = payload;
        this.createTime = System.currentTimeMillis();
        this.msgId = UUID.randomUUID().toString();
    }

    /**
     * Msg 의 parameter 없는 생성자
     */
    public Msg(){}

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public JsonNode getJSON(){

        return JSONUtils.parseJson(this.toString());
    }
    @Override
    public String toString() {
        
        return "{" + "topic : \"" + topic +"\", "
                   + "createTime : " + createTime + ","
                   + "msgId : \"" + msgId +"\", "
                   + "payload :" + payload + "}";
    }
}   
