package com.nhnacademy.aiot.util;

import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class Config {

    public static Properties properties = new Properties();

    public void set(String[] args) {

        Options options = new Options();
        options.addOption(null, "an", true, null);
        options.addOption("s", null, true, null);
        options.addOption("c", null, true, null);
        CommandLineParser cParser = new DefaultParser();

        try {
            CommandLine commandLine = cParser.parse(options, args);

            if (commandLine.hasOption("an")) {
            }

            if (commandLine.hasOption("s")) {
                properties.setProperty("sensorTypes", commandLine.getOptionValue("s"));
            }

            if (commandLine.hasOption("c")) {
                String jsonFilePath = commandLine.getOptionValue("c");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}