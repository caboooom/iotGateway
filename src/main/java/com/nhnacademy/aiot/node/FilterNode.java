//package com.nhnacademy.aiot.node;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.aiot.Msg;
//import com.nhnacademy.aiot.Wire;
//import lombok.extern.log4j.Log4j2;
//import org.json.simple.JSONObject;
//
//// Filter 는 SensorTypeFilterNode 처럼 개별로 만들지 말고,
//// FilterNode를 만들고 여기에 설정을 통해서 SensorType, Branch 등을 필터링 할 수 있도록 만들어 보세요
//// TODO Branch
//
//@Log4j2
//public class FilterNode extends Node{
//    String[] sensorTypes;
//    ObjectMapper objectMapper = new ObjectMapper();
//    static JsonNode payload;
//
//    public FilterNode(int inputPortCount, int outputPortCount, String[] sensorTypes) {
//        super(inputPortCount, outputPortCount);
//        this.sensorTypes = sensorTypes;
//    }
//
//    @Override
//    public void process() {
//        for (Wire wire : inputPorts[0].getWires()){
//            if (wire.hasMessage()){
//                Msg msg = wire.get();
//                try {
//                    processMsg(msg.toString(), sensorTypes);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//
//    private void processMsg(String msgString, String[] sensorTypes) throws JsonProcessingException {
//        payload = objectMapper.readTree(msgString);
//
//        for (String sensorType : sensorTypes) {
//            Double value = payload.path("object").path(sensorType).asDouble();
//            String deviceId = getData("object", "deviceInfo", "devEui");
//            String place = getData("deviceInfo", "tags", "place");
//
//            Msg outMsg = createMessage(deviceId, place, sensorType, value);
//            out(outMsg);
//        }
//    }
//
//    private String getData(String... paths) {
//        JsonNode result = payload;
//        for (String path : paths) {
//            result = result.path(path);
//        }
//
//        return result.asText();
//    }
//
//
//    private Msg createMessage(String deviceId, String place, String sensor, Double sensorValue){
//        Msg outMsg = new Msg();
//        outMsg.setTopic("/d/" + deviceId + "/p/" + place + "/e/" + sensor);
//
//        JSONObject outPayload = new JSONObject();
//        outPayload.put("time", System.currentTimeMillis());
//        outPayload.put(sensor, sensorValue);
//        outMsg.setPayload(outPayload);
//
//        return outMsg;
//    }
//
//}
