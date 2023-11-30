package com.nhnacademy.aiot.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import static com.nhnacademy.aiot.util.PropertiesKeys.*;

public class Config {
    String[] args;
    private static final Properties properties = new Properties();

    public Config(String[] args) {
        this.args = args;
        initiateProperties();
    }
    
    public static String getProperty(String key){

        return properties.getProperty(key);
    }

    public void initiateProperties() {

        properties.setProperty(APPLICATION_NAME.getKey() ,  "+");
    }

    public void set() {

        Options options = new Options();

        options.addOption(null, "an", true, null);
        options.addOption("s", null, true, "Argument는 ','로 구분된 문자열로 주어짐");
        options.addOption("c", null, true, "JSON 형식의 설정파일을 argument로 받는다.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption("c")){
                String filePath = commandLine.getOptionValue("c");

               
                  JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
 
                    if (jsonObject.get(PropertiesKeys.APPLICATION_NAME.getKey()) != null){
                        properties.setProperty(PropertiesKeys.APPLICATION_NAME.getKey(), jsonObject.get(PropertiesKeys.APPLICATION_NAME.getKey()).toString());
                     }
                    if (jsonObject.get(PropertiesKeys.SENSOR_TYPES.getKey()) != null){
                        String sensorListStr = jsonObject.getJSONArray(PropertiesKeys.SENSOR_TYPES.getKey()).toString(); // " ['temperature', 'humidity', 'co2'] "
                        properties.setProperty(PropertiesKeys.SENSOR_TYPES.getKey(), sensorListStr.substring(1, sensorListStr.length()-1)); // " 'temperature', 'humidity', 'co2' "
 
                    }

            }
            
            // 요구사항: 설정 파일과 cmd line argument가 함께 주어질 경우 cmd line argument가 우선된다.
            // 따라서 겹치는  내용이 있으면 cmd l ine argument가 기존의 내용을 덮어쓴다.
            if(commandLine.hasOption("an")){
                properties.setProperty("applicationName", commandLine.getOptionValue("an"));
            }

            if (commandLine.hasOption("s")){
                properties.setProperty("sensorTypes", commandLine.getOptionValue("s"));
            }  
            
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e){

        } catch (org.json.simple.parser.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

        