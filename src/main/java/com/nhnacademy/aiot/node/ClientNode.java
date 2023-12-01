package com.nhnacademy.aiot.node;

public class ClientNode extends Node{

    protected ClientNode(int inputPortCount, int outputPortCount) {
        super(inputPortCount, outputPortCount);
        //TODO Auto-generated constructor stub
    }

    //이팀도 MQTT와 같은 

    //클라이언트를 별도의 내부 노드로 만들고, 
    //이와 연결된 MqttInNode, MqttOutNode를 만들었으면 합니다. Node-Red를 잘 보시면, client는 별도로 생성하고, 여기에 연결하도록 하고 있습니다.
    //내부 노드와 MqttInNode/MqttOutNode간의 통신은 별도의 내부 wire를 구성해도 되고, MessageQueue방식등을 이용할 수도 있습니다.

    // public ClientNode(){

    // }
}
