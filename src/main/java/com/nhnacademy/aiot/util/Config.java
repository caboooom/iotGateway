package com.nhnacademy.aiot.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.cli.Options;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.enums.CmdOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class Config {

    String[] args;
    private static final String SINGLE_LEVEL_WILDCARD = "+";
    private static final Properties properties = new Properties();

    public Config(String[] args) {
        this.args = args;
        initiateProperties();
    }

    public static String getProperty(String key) {

        return properties.getProperty(key);
    }

    public void initiateProperties() {

        properties.setProperty(CmdOptions.APPLICATION_NAME.getKey(), SINGLE_LEVEL_WILDCARD);
    }

    public void set() {

        Options options = new Options();

        options.addOption(null, CmdOptions.APPLICATION_NAME.getValue(), true, null);
        options.addOption(CmdOptions.SENSOR_TYPES.getValue(), null, true,
                CmdOptions.SENSOR_TYPES.getDescription());
        options.addOption(CmdOptions.CONFIG_FILE.getValue(), null, true,
                CmdOptions.CONFIG_FILE.getDescription());

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption(CmdOptions.CONFIG_FILE.getValue())) {
                String filePath = commandLine.getOptionValue(CmdOptions.CONFIG_FILE.getValue());

                JSONParser jsonParser = JSONUtils.getParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));

                if (jsonObject.get(CmdOptions.APPLICATION_NAME.getKey()) != null) {
                    properties.setProperty(CmdOptions.APPLICATION_NAME.getKey(),
                            jsonObject.get(CmdOptions.APPLICATION_NAME.getKey()).toString());
                }
                
                if (jsonObject.get(CmdOptions.SENSOR_TYPES.getKey()) != null) {
                    String sensorTypes =
                            ((JSONArray) jsonObject.get(CmdOptions.SENSOR_TYPES.getKey()))
                                    .toString();
                    sensorTypes = sensorTypes.substring(1, sensorTypes.length() - 1);
                    properties.setProperty(CmdOptions.SENSOR_TYPES.getKey(), sensorTypes);
                }

                // 요구사항: 설정 파일과 cmd line argument가 함께 주어질 경우 cmd line argument가 우선된다.
                // 따라서 겹치는 내용이 있으면 cmd l ine argument가 기존의 내용을 덮어쓴다.
                if (commandLine.hasOption(CmdOptions.APPLICATION_NAME.getValue())) {
                    properties.setProperty(CmdOptions.APPLICATION_NAME.getKey(),
                            commandLine.getOptionValue(CmdOptions.APPLICATION_NAME.getValue()));
                }

                if (commandLine.hasOption(CmdOptions.SENSOR_TYPES.getValue())) {
                    properties.setProperty(CmdOptions.SENSOR_TYPES.getKey(),
                            commandLine.getOptionValue(CmdOptions.SENSOR_TYPES.getValue()));
                }

            }
        } catch (ParseException | org.apache.commons.cli.ParseException | IOException e) {

            e.printStackTrace();
        } 
    }
}

