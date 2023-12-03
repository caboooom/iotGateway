package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;

public class ReplaceNode extends Node{

    private String[] replaceTargets;
    private String replacement;
    
    protected ReplaceNode(String id, int outputPortCount, String[] replaceTargets, String replacement) {
        super(id, true , outputPortCount);
        this.replaceTargets = replaceTargets;
        this.replacement = replacement;
    }

    @Override
    public void process() {

        if(!inputPort.hasMessage()) return;

        Msg msg = inputPort.getMsg();
        String stringPayload = msg.getPayload().toString();

        for (String target : replaceTargets) {
            stringPayload = stringPayload.replaceAll(target, replacement);
        }
        
        msg.setPayload(JSONUtils.parseJson(stringPayload));
        out(msg);

    }


}
