package com.nhnacademy.aiot.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.util.JSONUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class FilterNode extends Node {

    private Set<String> targetStrings;

    public FilterNode(int outputPortCount, String[] targetStrings) {
        super(outputPortCount);
        this.targetStrings = new HashSet<>();

        for (String targetString : targetStrings) {
            this.targetStrings.add(targetString);
        }
    }

    @Override
    public void process() {
        if (!inputPort.hasMessage()) {
            return;
        }
        Msg inMsg = inputPort.getMsg();
        JsonNode inPayload = inMsg.getPayload();
        ObjectNode outPayload = createFilteredPayload(inPayload);

        Msg outMsg = new Msg("", outPayload);
        out(outMsg);
    }


    /**
     * @param inPayload
     * @return outPayload
     * 나가는 Msg의 payload를 만들고 filterJsonNode 메서드를 호출해
     * 필터링된 payload를 return하는 메서드
     */
    private ObjectNode createFilteredPayload(JsonNode inPayload) {
        ObjectNode outPayload = JSONUtils.getMapper().createObjectNode();

        filterJsonNode(inPayload, outPayload);

        return outPayload;
    }

    /**
     * @param inPayload
     * @param outPayload
     * 들어온 Msg의 payload에서 targerStrings에 있는 문자열과 일치하는 Key가 있다면
     * 나가는 Msg의 payload에 Key와 Value를 저장하는 메서드
     * 
     */
    private void filterJsonNode(JsonNode inPayload, ObjectNode outPayload) {
        Iterator<Entry<String, JsonNode>> entryIterator = inPayload.fields();
        while (entryIterator.hasNext()) {
            Entry<String, JsonNode> entry = entryIterator.next();

            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            if (targetStrings.contains(fieldName)) {
                outPayload.set(fieldName, fieldValue);
            }
            if (fieldValue.isObject()) {
                filterJsonNode(fieldValue, outPayload);
            }

        }
    }

}
