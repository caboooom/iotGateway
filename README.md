
💡 LoRa 디바이스를 관리하는 Chirpstack 서버와 연동하여 센서 데이터를 수집하는 Gateway를 구현하였습니다. <br>
<br>


# 목차
 - [개요](#개요)
 - [flows.json](#flowsjson)
 - [클래스 다이어그램](#클래스-다이어그램)
 - [사용한 라이브러리 목록](#사용한-라이브러리-목록)
 <br>

# 개요

 - 프로젝트 이름 : IoT Gateway
 - 프로젝트 기간 : 2023.11.28 - 2023.12.
 - 멤버 : 박상진, 신민석, 유창은, 임세연
 - 개발 언어 : Java
 - 개발 환경 : Visual Studio Code
 - 연동 서버 : `tcp://ems.nhnacademy.com:1883`
 - 메시징 프로토콜: MQTT
 <br>


# flows.json

 - 전체 노드들과 설정 정보를 가지고 있는 파일이다.
 - 반드시 JSON 형식으로 작성되어야 한다.
 - 경로 및 파일명이 반드시 `src/main/java/com/nhnacademy/aiot/flows.json`이어야 한다.
 - 하나의 JSON 배열에 모든 노드들이 포함되어야 한다.

 - flows.json 파일 작성 예시:
```
[
    {
        "id" : "1111",
        "nodeType" : "MqttInNode",
        "topic" : "application/#",
        "qos" : 1,
        "broker" : "2222",
        "wires" : [
            ["3333"]
        ]
    },
    {
        "id" : "2222",
        "nodeType" : "ClientNode",
        "broker" : "tcp://ems.nhnacademy.com",
        "port" : 1883,
        "clientId" : "abc123",
        "autoConnect": true,
        "cleansession": true,
        "keepalive": "60"
    },
    {
        "id" : "3333",
        "nodeType" : "FilterNode",
        "targetStrings" : ["devEui", "place", "object", "branch"],

        "wires" : [
            ["4444"]
        ]

    },
    {
        "id" : "4444",
        "nodeType" : "SplitNode",
        "splitKey" : "object",
        "keyHolder" : "sensorType",
        "wires" : [
            ["5555"]
        ]
    },
    {
        "id" : "5555",
        "nodeType" : "GenerateTopicNode",
        "topicPattern" : "data/d/+/b/+/p/+/e/+",
        "field" : ["devEui", "branch", "place", "sensorType"],
        "wires" : [
            ["6666"]
        ]
    },
    {
        "id" : "6666",
        "nodeType" : "ReplaceNode",
        "replaceTargets" : ["humidity", "temperature", "co2", "tvoc"], 
        "replacement" : "value",
        "wires" : [
            ["7777"]
        ]
    },
    {
        "id" : "7777",
        "nodeType" : "SwitchNode",
        "targetKeys" : ["devEui", "branch", "place", "sensorType" ,"value"] ,
        "wires" : [
            ["8888"]
        ]
    },
    {
        "id" : "8888",
        "nodeType" : "FilterNode",
        "targetStrings" : ["time", "value"],
        "wires" : [
            ["9999","10"]
        ]
    },
    
    {
        "id" : "9999",
        "nodeType" : "DebugNode",
        "wires" : []
    },
    {
        "id" : "10",
        "nodeType" : "MqttOutNode",
        "topic" : "", 
        "qos" : 2,
        "broker" : "11",
        "wires" : []

    },
    {
        "id" : "11",
        "nodeType" : "ClientNode",
        "broker" : "tcp://localhost",
        "port" : 1883,
        "clientId" : "def456",
        "autoConnect": true,
        "cleansession": true,
        "keepalive": "60"
    }
]
```
 - 예시 파일을 지정 경로에 넣고 프로그램을 실행하면 다음과 같은 flow가 생성됩니다.
   ![image](https://github.com/caboooom/iotGateway/assets/124178635/0fb20d74-f537-4ccf-9540-d53a852856cc)

<br>

# 클래스 다이어그램
![image](https://github.com/caboooom/iotGateway/assets/124178635/e579ef60-26c1-4981-b055-52078ef6310c)

### Main.java
 - FlowGenerator 객체를 생성하여 실행한다.

### FlowGenerator.java
 - `flows.json` 파일을 읽어 필요한 노드, 포트, 와이어를 동적으로 생성하고, 연결하여 실행한다.

### ClientNode.java
 - 생성된 후 MqttInNode 또는 MqttOutNode에 주입된다.
 - Mqtt 클라이언트를 생성하여, 서버에 연결하고 publish/subscribe 작업을 수행한다.

### MqttInNode.java
 - 내부에 clientNode를 가지고 있다.
 - clientNode는 외부에서 별도로 생성되어 주입된다.
   
 - clientNode는 서버로부터 MQTT 메시지를 수신한다.
 - 수신한 데이터를 전처리하여, 다음 노드에게 전달한다.

### FilterNode.java
 - 문자열 배열인 targetStrings의 키를 기준으로 해당 key:value 쌍만 남기고, 다음 노드에게 전달한다.

### SplitNode.java

- splitKey와 keyHolder를 기준으로 필요한 정보를 추출하여, splitKey의 값에 해당하는 키 개수만큼의 payload로 분리해준다.

 - 예를 들어, splitKey="object", keyHolder="sensorType"로 설정하는 경우, 다음과 같이 동작한다.

<img width="800" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/1bd796a9-fec9-4c33-ad3e-85b420a39edd">
   

### GenerateTopicNode.java

 - MqttOutNode에서 연결 서버로 publish할 메시지의 topic을 생성한다.

### ReplaceNode.java

 - 변경할 문자열이 담긴 배열인 replaceTargets와 replacement 정보를 받는다.
 - 메시지의 payload의 key 중 replaceTarget이 존재하면, 해당 키를 replacement로 변경한다.

### SwitchNode.java
 - 이전 노드에서 수신한 메시지의 payload가 주어진 targetKeySet의 모든 키를 갖고 있는 경우, 다음 노드로 전달한다.
 - 그렇지 않으면, 작업을 종료한다.

### MqttOutNode.java

 - 내부에 clientNode를 가지고 있다.
 - clientNode는 외부에서 별도로 생성되어 주입된다.
   
 - clientNode는 MQTT 메시지를 publish하여 localhost가 수신할 수 있도록 한다.
 - localhost에서 수신한 해당 데이터들은 influxDB에 저장된다.

### DebugNode.java
 - 입력, 출력, 비정상 처리된 패킷 수를 로그로 남긴다.

<br>

# 사용한 라이브러리 목록

- [Jackson Databind](https://github.com/FasterXML/jackson-databind)
- [Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Log4j API](https://logging.apache.org/log4j/2.x/log4j-api/)
- [Log4j Core](https://logging.apache.org/log4j/2.x/log4j-core/)
- [Eclipse Paho](https://www.eclipse.org/paho/)
- [Project Lombok](https://projectlombok.org/)
- [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/)
- [JUnit Jupiter Engine](https://junit.org/junit5/docs/current/user-guide/)
