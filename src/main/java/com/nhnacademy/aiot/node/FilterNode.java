package com.nhnacademy.aiot.node;

import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiot.Msg;

public class FilterNode extends Node {

    String[] targetStrings;

    protected FilterNode(int inputPortCount, int outputPortCount, String[] targetStrings) {
        super(inputPortCount, outputPortCount);
        this.targetStrings = targetStrings;
    }


    @Override
    public void preprocess() {
        String[] targetStrings = {"id", "deviceId"};
    }

    @Override
    public void process() {
        if (!inputPorts[0].hasMessage()) {
            return;
        }
        Msg inMsg = inputPorts[0].getMsg();

        Msg outMsg = new Msg();

        // JSONObject inPayload = inMsg.getPayload();

        for (String targetString : targetStrings) {
            // inPayload.get(targetString);
        }
    }

    /***
     * 
     * @param payload
     * @param key
     * 
     * 들어온 페이로드를 Key를 기준으로 새로 생성?
     */
    private static void spliter(JsonNode payload, String key) {
        try {
            if (!payload.has(key)) {
                return;
            }
            JsonNode objectNode = payload.get(key);

            if (objectNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    Msg newMsg = createMsg(payload, key, fieldName, fieldValue);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Msg createMsg(JsonNode payload, String key, String fieldName, JsonNode fieldValue) {
        Msg newMsg = new Msg();

        
        // JSONObject sJsonObject = Msg.getPayload();
        // newPayload.put("payload", payload);
        // newPayload.put(fieldName, fieldValue.asInt());
        

        // newMsg.setPayload();
        // System.out.println(newMsg + "Asdasdas");
        return newMsg;
    }

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode payload =
                mapper.readTree("{\n" + "  \"deviceId\": \"xxxx\",\n" + "  \"place\": \"xxxxx\",\n"
                        + "  \"branch\": \"gyungnam\",\n" + "  \"object\": {\n"
                        + "    \"temperature\": 0,\n" + "    \"humidity\": 0\n" + "  }\n" + "}");

        System.out.println("ASdasdas");

        spliter(payload, "object");
    }


}
