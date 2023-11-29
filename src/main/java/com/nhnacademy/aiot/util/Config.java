package com.nhnacademy.aiot.util;

import java.io.FileReader;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {

    public static Properties properties = new Properties();
    public static String[] sensors;

    public void set(String[] args) {

        Options options = new Options();
        options.addOption(null, "an", true, null);
        options.addOption("s", null, true, null);
        options.addOption("c", null, true, null);
        CommandLineParser cParser = new DefaultParser();

        try {
            CommandLine commandLine = cParser.parse(options, args);

            if (commandLine.hasOption("an")) {
                System.out.println("-ac");
            }

            if (commandLine.hasOption("s")) {
                properties.setProperty("sensorTypes", commandLine.getOptionValue("s"));
            }

            if (commandLine.hasOption("c")) {
                String jsonFilePath = commandLine.getOptionValue("c");
                readConfigFromJson(jsonFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readConfigFromJson(String jsonFilePath) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            
            //설정을 읽어서 Properties에 추가
            String sensorTypes = (String) jsonObject.get("sensorTypes");
            if (sensorTypes != null) {
                properties.setProperty("sensorTypes", sensorTypes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}