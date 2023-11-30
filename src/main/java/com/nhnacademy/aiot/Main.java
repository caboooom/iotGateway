package com.nhnacademy.aiot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.nio.charset.StandardCharsets;

public class Main {
    static String broker = "tcp://ems.nhnacademy.com:1883";
    static String clientId = "JavaMqttClient";
    static String topics = "#";

    // Todo 조건문 -> 다형성
    // Todo CLI

    public static void main(String[] args) throws MqttException {

        try {
            IMqttClient publisher = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            ObjectMapper objm = new ObjectMapper();

            connectToBroker(options, publisher);
            mqttMethod(publisher);
            publisher.subscribe(topics, (topic, msg) -> {

                String[] topicList  = topic.split("/");

                // TODO Json Parsing
                // TODO split 없이 사용할 수 있는 mqtt Method 찾기

                if ("application".equals(topicList[0])){
                    String jsonString = msg.toString();
                    JsonNode jsonNode = objm.readTree(jsonString);

                    // Todo 필요한 값 추출
                    String time = jsonNode.path("time").asText();
                    String humidity = jsonNode.path("object").path("humidity").asText();
                    String temperature = jsonNode.path("object").path("temperature").asText();
                    String co2 = jsonNode.path("object").path("co2").asText();
                    String leq = jsonNode.path("object").path("leq").asText();
                    String lmax = jsonNode.path("object").path("lmax").asText();
                    String tvoc = jsonNode.path("object").path("tvoc").asText();

                    System.out.println("시간: " + time);
                    System.out.println("습도: " + humidity);
                    System.out.println("온도: " + temperature);
                    System.out.println("co2: " + co2);
                    System.out.println("leq: " + leq);
                    System.out.println("lmax: " +lmax);
                    System.out.println("tvoc: " + tvoc);
                }

            });

            while (!Thread.interrupted()) {
                Thread.sleep(1000);
            }

            publisher.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mqttMethod(IMqttClient publisher) {

        publisher.setCallback(new MqttCallback() {

            //MQTT Broker 연결이 끊길때
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Disconnected");
            }

            // 구독한 토픽에 새로운 메시지가 도착했을 때 호출.
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String payloadString = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
            }

            // 메시지 전송이 끝났을 때, qos 1 or 2에서만 사용됨
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("전송완료");
            }
        });
    }

    private static void connectToBroker(MqttConnectOptions options, IMqttClient publisher) throws MqttException {
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setWill("willTopic", "연결끊김".getBytes(), 1, false);
        System.out.println("Connecting to Broker:" + broker);
        publisher.connect(options);
        System.out.println("연결 완료");
    }
}

