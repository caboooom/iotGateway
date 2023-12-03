쓰는즁

LoRa 디바이스를 관리하는 Chirpstack 서버와 연동하여 센서 데이터를 수집하는 Gateway를 구현하였습니다. 
<br>

# 목차
 - 개요
 - 전체 flow
 - 사용한 라이브러리 목록
 <br>

# 개요

 - 프로젝트 이름 : IoT Gateway
 - 프로젝트 기간 : 2023.11.28 - 2023.12.03
 - 개발 언어 : JAVA
 - 개발 환경 : VSCODE
 - 멤버 : 박상진, 신민석, 유창은, 임세연
 <br>


# flows.json 파일 양식

 - flows.json 파일
      - 반드시 JSON 형식으로 작성되어야 한다.
      - 경로 및 파일명이 반드시 `src/main/java/com/nhnacademy/aiot/flows.json`이어야 한다.
      - 하나의 JSON 배열에 모든 노드들이 포함되어야 한다.

      - 대부분의 노드들이 기본적으로 가지는 key
        - id : 노드 아이디
        - nodeType : 노드 종류 (클래스명)
        - wires :

     - flows.json 파일 예시
```
     [
    {
        "id" : "1111",
        "nodeType" : "mqttInNode",
        "topic" : "",
        "qos" : 1,
        "broker" : "2222",
        "wires" : [
            [ "3333"]
        ]
    },
    {
        "id" : "2222",
        "nodeType" : "mqttBroker",
        "broker" : "tcp://ems.nhnacademy.com",
        "port" : 1883,
        "clientId" : "abc123",
        "autoConnect": true,
        "cleansession": true,
        "keepalive": "60"
    },
    {
        "id" : "3333",
        "nodeType" : "filterNode",
        "targetKeys" : [],

        "wires" : [
            ["4444"]
        ]

    },
    {
        "id" : "4444",
        "nodeType" : "mqttOutNode",
        "topic" : "", 
        "qos" : 2,
        "broker" : "5555",
        "wires" : []

    },
    {
        "id" : "5555",
        "nodeType" : "mqttBroker",
        "broker" : "mosquitto",
        "port" : 1883,
        "clientId" : "def456",
        "autoConnect": true,
        "cleansession": true,
        "keepalive": "60"

    }
    ]
```

<br>

# 전체 flow

 - 연동 서버 : tcp://ems.nhnacademy.com:1883

 - 메시징 프로토콜: MQTT

![Flow예시수정예정](https://github.com/caboooom/iotGateway/assets/124178635/a34a4278-7fac-4576-a7d3-3316510dcba6)
(이미지는 수정할 예정)


#### Config.java
 - Command line argument 또는 별도 설정파일로 옵션을 지정했을 경우, 이를 처리한다.


#### MqttInNode.java
 - 내부에 clientNode를 가지고 있고, clientNode가 서버로부터 MQTT 메시지를 수신한다.
 - 수신한 데이터를 전처리하여, 다음 노드에게 전달한다.
 
#### FilterNode.java
 - 구독하고자 하는 토픽의 데이터만 받아서, 다음 노드에게 전달한다.

### SplitNode.java
 - 데이터 전처리 !!

#### MqttOutNode.java
 - 내부에 clientNode를 가지고 있다.
 - clientNode는 MQTT 메시지를 publish하여 localhost가 수신할 수 있도록 한다.
 - localhost에서 수신한 해당 데이터들은 influxDB에 저장한다.

#### DebugNode.java
 - 입력, 출력, 비정상 처리된 패킷 수를 남긴다.

<br>

# 사용한 라이브러리 목록
