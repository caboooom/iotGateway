package com.nhnacademy.aiot.node;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.enums.CmdOptions;
import com.nhnacademy.aiot.util.Config;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttInNode extends Node {

    private final String TOPIC;

    public MqttInNode(int outputWireCount) {
        super(0, outputWireCount);
        this.TOPIC = "application/" + Config.getProperty(CmdOptions.APPLICATION_NAME.getKey())
                + "/device/+/+/up";
    }

    public Msg createMsg(String topic, String payload) {
        if (JSONUtils.isJson(payload)) {
            try {
                JSONObject jsonObject = (JSONObject) JSONUtils.getParser().parse(payload);
                return new Msg(topic, jsonObject);

            } catch (ParseException e) {
                // error count++
            }
        }
        return null;
    }

    @Override
    public void process() {
        String publisherId = UUID.randomUUID().toString();
        try (IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com:1883", publisherId)) {
            client.connect();
            setMqttOptions();

            CountDownLatch receivedSignal = new CountDownLatch(50);

            IMqttMessageListener listener = (topic, msg) -> {
                String payload = new String(msg.getPayload());
                Msg outMsg = createMsg(topic, payload);
                if (outMsg != null) {
                    out(outMsg);
                }
                receivedSignal.countDown();
            };
            client.subscribe(TOPIC, listener);
            
            receivedSignal.await(1, TimeUnit.MINUTES);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } 
        catch (InterruptedException e) {
            log.warn("");
            Thread.currentThread().interrupt();
        }

    }

    private MqttConnectOptions setMqttOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setWill("test/will", "Disconnected".getBytes(), 1, false);
        return options;
    }



}
