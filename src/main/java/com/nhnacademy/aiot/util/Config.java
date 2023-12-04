package com.nhnacademy.aiot.util;

import java.io.FileReader;
import java.util.Properties;
import org.apache.commons.cli.Options;
import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.aiot.enums.CmdOptions;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

@Log4j2
public class Config {

    String[] args; // args 를 담는 문자열 배열
    private static final String SINGLE_LEVEL_WILDCARD = "+";
    private static final Properties properties = new Properties();

    public Config(String[] args) {
        this.args = args;
        initProperties();
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void initProperties() {
        properties.setProperty(CmdOptions.APPLICATION_NAME.getKey(), SINGLE_LEVEL_WILDCARD);
    }

    private void setProperty(String key, String value) {
        properties.setProperty(key, value);
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
                JsonNode jsonObject = JSONUtils.parseJson(new FileReader(filePath));

                if (jsonObject.get(CmdOptions.APPLICATION_NAME.getKey()) != null) {
                    setProperty(CmdOptions.APPLICATION_NAME.getKey(),
                            jsonObject.get(CmdOptions.APPLICATION_NAME.getKey()).toString());
                }

                if (jsonObject.get(CmdOptions.SENSOR_TYPES.getKey()) != null) {
                    String sensorTypes = jsonObject.get(CmdOptions.SENSOR_TYPES.getKey()).asText();
                    sensorTypes = sensorTypes.substring(1, sensorTypes.length() - 1);
                    setProperty(CmdOptions.SENSOR_TYPES.getKey(), sensorTypes);
                }
            }

            // 요구사항: 설정 파일과 cmd line argument가 함께 주어질 경우 cmd line argument가 우선된다.
            // 따라서 겹치는 내용이 있으면 cmd l ine argument가 기존의 내용을 덮어쓴다.
            if (commandLine.hasOption(CmdOptions.APPLICATION_NAME.getValue())) {
                setProperty(CmdOptions.APPLICATION_NAME.getKey(),
                        commandLine.getOptionValue(CmdOptions.APPLICATION_NAME.getValue()));
            }

            if (commandLine.hasOption(CmdOptions.SENSOR_TYPES.getValue())) {

                setProperty(CmdOptions.SENSOR_TYPES.getKey(),
                        commandLine.getOptionValue(CmdOptions.SENSOR_TYPES.getValue()));
            }



        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}

