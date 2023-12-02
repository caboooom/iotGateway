package com.nhnacademy.aiot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.util.JSONUtils;

import lombok.extern.log4j.Log4j2;



@Log4j2
public class FlowGenerator {

    private final String FILE_PATH = "src/main/resources/flowsss.json";
    private final String NODE_ID = "id";
    private final String NODE_PATH = "com.nhnacademy.aiot.node.";
    private final String NODE_TYPE = "nodeType";

    static Map<String, Object> nodeMap = new HashMap<>();
    static Map<String, Wire> wireList = new HashMap<>();


    FlowGenerator(){}

    public void generateNodes(){
        JsonNode jsonObject;
        try {
            jsonObject = JSONUtils.parseJson(new FileReader(FILE_PATH));
            for(JsonNode j : jsonObject){
            
                
                String nodeId = j.path(NODE_ID).asText();

                String nodeType = j.path(NODE_TYPE).asText();


                String name = NODE_PATH + nodeType;

                Class clazz = Class.forName(name);

                Constructor constructor = clazz.getConstructor(JsonNode.class);
                Object instance = constructor.newInstance(j);                
                System.out.println("created instance" + instance.toString());
                nodeMap.put(nodeId, instance);                
        }
        } catch (FileNotFoundException e) {
            log.error("Main - Cannot find file 'flows.json'");
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {

        }catch (ClassNotFoundException e) {
                    log.error("class not found");
        } catch(SecurityException e){

        }

        
}


    public void generateOutWires() {

        try {
            JsonNode jsonObject = JSONUtils.parseJson(new FileReader(FILE_PATH));
            
            for(JsonNode jsonNode : jsonObject){

                System.out.println(jsonNode.get("nodeType").asText());

                String nodeId = jsonNode.path("id").asText();
                System.out.println(nodeId);
                JsonNode wires = jsonNode.get("wires");
               
                Node node = (Node)(nodeMap.get(nodeId));
                System.out.println(node.toString());


            int portNum = -1;
            for (JsonNode wire : wires) {
                ++portNum;
                System.out.println("포트" + portNum);
                
                for (JsonNode element : wire) {
                    Wire w = new Wire();
                    node.setOutputWire(portNum, w);
                    System.out.println("  내부 와이어 -> " + element.asText());
                    wireList.put(element.asText(), w);
                }
            }
        
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void connectWires() {
        for(Map.Entry<String, Wire> entry : wireList.entrySet()){
            Node targetNode = (Node)nodeMap.get(entry.getKey());
            targetNode.setInputWire(entry.getValue());
        }
    }

    public void start() {
    }
}
