package com.nhnacademy.aiot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.node.ClientNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.util.JSONUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FlowGenerator {

    private static final String FILE_PATH = "src/main/resources/flows.json";
    private static final String NODE_ID = "id";
    private static final String NODE_PATH = "com.nhnacademy.aiot.node.";
    private static final String NODE_TYPE = "nodeType";
    private static final String WIRES = "wires";
    private static final String CLIENT_NODE_ID = "broker";

    static Map<String, Object> nodeMap = new HashMap<>(); // < nodeId, node instance >
    static Map<String, Wire> wireMap = new HashMap<>(); // < nodeId, wire instance >

    private JsonNode allJsonNodes = null;

    FlowGenerator() {
        try {
            allJsonNodes = JSONUtils.parseJson(new FileReader(FILE_PATH));
        } catch (FileNotFoundException e) {
            log.error("Cannot find file 'flows.json'", e.getMessage());
        } catch (IllegalArgumentException | SecurityException e) {
            log.error("FlowGenerator() - ", e.getMessage());
        }
    }

    /*
     * Reflection을 통해 flows.json 에 명시된 노드들을 생성해주는 메서드
     */
    public void generateNodes() {
        try {
            for (JsonNode jsonNode : allJsonNodes) {

                String nodeId = jsonNode.path(NODE_ID).asText();
                String nodeType = jsonNode.path(NODE_TYPE).asText();

                Class<?> clazz = Class.forName(NODE_PATH + nodeType);
                Constructor<?> constructor = clazz.getConstructor(JsonNode.class);
                Object instance = constructor.newInstance(jsonNode);

                nodeMap.put(nodeId, instance);
            }
        } catch (ReflectiveOperationException e) {
            log.error("FlowGenerator generateNodes() error - " + e.getMessage());
        }
    }

    /*
    * 각 노드의 outputWire를 생성하고 outputPort에 연결한 후,
    * 각 와이어를 해당 와이어와 연결할 inputPort를 가진 노드 아이디와 맵핑하여 wireMap에 저장하는 메서드
    */
    public void generateOutputWires() {

        for (JsonNode jsonNode : allJsonNodes) {

            if (jsonNode.get(WIRES) == null)
                continue;

            String nodeId = jsonNode.path(NODE_ID).asText();
            Node node = (Node) (nodeMap.get(nodeId));

            JsonNode nodeConnectionsByPort = jsonNode.get(WIRES);

            int portNum = 0;
            for (JsonNode port : nodeConnectionsByPort) {
                for (JsonNode targetNodeId : port) {
                    Wire w = new Wire();
                    node.setOutputWire(portNum, w);
                    wireMap.put(targetNodeId.asText(), w);
                }
                portNum++;
            }
        }

    }

    /*
    * wireMap을 순회하며 각 노드의 inputPort에 와이어를 연결시켜주는 메서드
    * wireMap은 key로 nodeId, value로 wire를 가진다.
    */
    public void connectWires() {
        for (Map.Entry<String, Wire> entry : wireMap.entrySet()) {
            Node targetNode = (Node) nodeMap.get(entry.getKey());
            targetNode.setInputWire(entry.getValue());
        }
    }

    /*
     * nodeMap에 있는 모든 노드를 실행시키는 메서드
     */
    public void start() {
        for (String key : nodeMap.keySet()) {
            if(nodeMap.get(key) instanceof ClientNode) continue; // ClientNode는 MqttInNode/MqttOutNode에서 start시킨다.
            ((Node) nodeMap.get(key)).start();
        }
    }

    /*
     * MqttInNode와 MqttOut노드의 경우, 내부 노드로 클라이언트 노드를 가진다.
     * 해당 노드들의 클라이언트 인스턴스를 nodeMap에서 찾아서 set해주는 메서드
     */
    public void injectClients() {

        for (JsonNode jsonNode : allJsonNodes) {

            String nodeType = jsonNode.path(NODE_TYPE).asText();
            String nodeId = jsonNode.path(NODE_ID).asText();

            if (nodeType.equals("MqttInNode")) {
                MqttInNode mqttInNode = (MqttInNode) nodeMap.get(nodeId);
                String clientId = jsonNode.path(CLIENT_NODE_ID).asText();
                mqttInNode.setClientNode((ClientNode) nodeMap.get(clientId));
            }

            if (nodeType.equals("MqttOutNode")) {
                MqttOutNode mqttOutNode = (MqttOutNode) nodeMap.get(nodeId);
                String clientId = jsonNode.path(CLIENT_NODE_ID).asText();
                mqttOutNode.setClientNode((ClientNode) nodeMap.get(clientId));
            }
        }

    }
}
