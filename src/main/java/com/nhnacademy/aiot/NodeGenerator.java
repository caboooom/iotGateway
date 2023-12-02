package com.nhnacademy.aiot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.util.JSONUtils;

import lombok.extern.log4j.Log4j2;



@Log4j2
public class NodeGenerator {

    private final String FILE_PATH = "src/main/resources/flows.json";
    private final String NODE_ID = "Id";
    private final String NODE_PATH = "com.nhnacademy.aiot.node.";
    private final String NODE_TYPE = "nodeType";

    Map<String, Object> nodeMap;


    NodeGenerator(){
        this.nodeMap = Main.nodeMap;
    }


    public void generateNodes(){
        JsonNode jsonObject;
        try {
            jsonObject = JSONUtils.parseJson(new FileReader(FILE_PATH));
            for(JsonNode j : jsonObject){
            
                
                String nodeId = j.path(NODE_ID).toString();

                String nodeType = j.path(NODE_TYPE).asText();

                System.out.println(nodeType);

                String name = NODE_PATH + nodeType;

                Class clazz = Class.forName(name);

                Constructor constructor = clazz.getConstructor(JsonNode.class);
                nodeMap.put(nodeId, constructor.newInstance(j));
                
                
        }
        } catch (FileNotFoundException e) {
            log.error("Main - Cannot find file 'flows.json");
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {

        }catch (ClassNotFoundException e) {
                    log.error("class not found");
        } catch(SecurityException e){

        }
}
}
