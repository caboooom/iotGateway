package com.nhnacademy.aiot.node;

import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;

public class ReplaceNode extends Node{

    private static final String NODE_ID = "id";
    private static final String WIRES = "wires";
    private static final String REPLACE_TARGETS = "replaceTargets";
    private static final String REPLACEMENT = "replacement";
    private String[] replaceTargets;
    private String replacementStr;
    
    public ReplaceNode(String id, int outputPortCount, String[] replaceTargets, String replacementStr) {
        super(id, true , outputPortCount);
        this.replaceTargets = replaceTargets;
        this.replacementStr = replacementStr;
    }

    public ReplaceNode(JsonNode jsonNode){
        this(jsonNode.path(NODE_ID).asText(), jsonNode.path(WIRES).size(),
            StreamSupport.stream(jsonNode.path(REPLACE_TARGETS).spliterator(), false)
                    .map(JsonNode::asText)
                    .toArray(String[]::new),
            jsonNode.path(REPLACEMENT).asText()
        );
    }

    @Override
    public void process() {

        if(!inputPort.hasMessage()) return;

        Msg msg = inputPort.getMsg();
        String stringPayload = msg.getPayload().toString();

        for (String target : replaceTargets) {
            stringPayload = stringPayload.replaceAll(target, replacementStr);
        }
        
        msg.setPayload(JSONUtils.parseJson(stringPayload));
        out(msg);

    }


}
