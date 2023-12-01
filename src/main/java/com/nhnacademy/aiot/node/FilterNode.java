package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiot.Msg;

public class FilterNode extends Node{

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

        //JSONObject inPayload = inMsg.getPayload();

        for (String targetString : targetStrings) {
            //inPayload.get(targetString);
        }
    }

    public static void main(String[] args) {
        String json = "[\n" + //
                "    {\n" + //
                "        \"id\": \"e8c7a4f2c8c382ad\",\n" + //
                "        \"type\": \"function\",\n" + //
                "        \"name\": \"function 16\",\n" + //
                "        \"outputs\": 1,\n" + //
                "        \"timeout\": 0,\n" + //
                "        \"noerr\": 0,\n" + //
                "        \"libs\": [],\n" + //
                "        \"wires\": [\n" + //
                "            [\n" + //
                "                \"8f0e3eeefa7c3e3f\",\n" + //
                "                \"dd6ec7f9a5ef4245\",\n" + //
                "                \"635770302db65da1\"\n" + //
                "            ]\n" + //
                "        ]\n" + //
                "    }\n" + //
                "]"; 

        String[] targets = {"DeviceInfo" , "Object"};
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.
            System.out.println(jsonNode);
            System.out.println(jsonNode.get("id"));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
