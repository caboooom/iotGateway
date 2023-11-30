package com.nhnacademy.aiot.util;

import java.io.FileReader;
import java.util.Properties;


import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class Config {

    String[] args; 
    public static Properties properties;
    
    public Config (String[] args){
        this.args = args;
        initiateProperties();
    }

    public void initiateProperties(){
        this.properties = new Properties();

        properties.setProperty("applicationName", "+");

    }

    public void set(){

        // Add options
        Options options = new Options();

        options.addOption(null, "an", true, null);
        options.addOption("s", null, true, "Argument는 ','로 구분된 문자열로 주어짐");
        options.addOption("c", null, true, "JSON 형식의 설정파일을 argument로 받는다.");


        // Parse options and set properties
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            //   설정파일 예시:
            //   { "applicationName" : "32fq8hqfq8983hqb932bvc",
            //     "sensors" : ["temperature", "humidity", "co2"] }
            if (commandLine.hasOption("c")){
                String filePath = commandLine.getOptionValue("c");

                try{
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));

                    if (jsonObject.get("applicationName") != null){
                        properties.setProperty("applicationName", jsonObject.get("applicationName").toString());
                    }
                    if (jsonObject.get("sensors") != null){
                        String sensorListStr = jsonObject.getJSONArray("sensors").toString(); // " ['temperature', 'humidity', 'co2'] "
                        properties.setProperty("sensorTypes", sensorListStr.substring(1, sensorListStr.length()-1)); // " 'temperature', 'humidity', 'co2' "

                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            // 요구사항: 설정 파일과 cmd line argument가 함께 주어질 경우 cmd line argument가 우선된다.
            // 따라서 겹치는 내용이 있으면 cmd line argument가 기존의 내용을 덮어쓴다.
            if(commandLine.hasOption("an")){
                properties.setProperty("applicationName", commandLine.getOptionValue("an"));
            }

            if (commandLine.hasOption("s")){
                properties.setProperty("sensorTypes", commandLine.getOptionValue("s"));
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
