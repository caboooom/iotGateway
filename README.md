
💡 LoRa 디바이스를 관리하는 Chirpstack 서버와 연동하여 센서 데이터를 수집하는 Gateway를 구현하였습니다. <br>
<br>


# 목차
 - [개요](#개요)
 - [flows.json](#flowsjson)
 - [동작 방식](#예시-파일-동작-방식)
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

 - `flows.json` 작성 예시:
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
            [ "6666"]
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
        "targetKeySet" : ["devEui", "branch", "place", "sensorType" ,"value"] ,
        "wires" : [
            ["8888"]
        ]
    },
    {
        "id" : "8888",
        "nodeType" : "FilterNode",
        "targetStrings" : ["time", "value"],
        "wires" : [
            ["9999", "10"]
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
#### 예시 파일을 지정 경로에 넣고 프로그램을 실행하면 다음과 같은 flow가 생성된다.
   ![image](https://github.com/caboooom/iotGateway/assets/124178635/0fb20d74-f537-4ccf-9540-d53a852856cc)
   
## 예시 파일 동작 방식

#### 1️⃣ Main.java 실행
 - FilterGenerator 인스턴스가 생성되고 실행된다.
 - FilerGenerator는 `flows.json` 파일을 읽어서 필요한 노드, 포트, 와이어를 동적으로 생성하고, 연결하여 실행한다.
<br>
   
#### 2️⃣ MqttInNode
 - 내부 노드인 ClientNode가 서버로부터 메시지를 받아온다. (ClientNode는 외부에서 별도로 생성되어 주입된다.)
   ```
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
    }
   ```
<br>

#### 3️⃣ FilterNode
 - targetStrings에 지정한 key에 해당하는 오브젝트들만 가져와 payload에 넣는다.
   ```
   {
        "id" : "3333",
        "nodeType" : "FilterNode",
        "targetStrings" : ["devEui", "place", "object", "branch"],
        "wires" : [
            ["4444"]
        ]

    }
   ```
 ![image](https://github.com/caboooom/iotGateway/assets/124178635/761145b0-1c51-4336-af66-5617c87ce9cf)
<br>
<br>

 #### 4️⃣ SplitNode
  - splitKey와 keyHolder를 기준으로 payload로 분리한다.

  ```
  {
        "id" : "4444",
        "nodeType" : "SplitNode",
        "splitKey" : "object",
        "keyHolder" : "sensorType",
        "wires" : [
            ["5555"]
        ]
    }
  ```
<img width="932" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/40e3d1ff-717f-41d9-9821-a619a3ddf860">

#### 5️⃣ GenerateTopicNode
 - MqttOutNode에서 publish할 메시지의 토픽을 생성한다.
   ```
   {
        "id" : "5555",
        "nodeType" : "GenerateTopicNode",
        "topicPattern" : "data/d/+/b/+/p/+/e/+",
        "field" : ["devEui", "branch", "place", "sensorType"],
        "wires" : [
            [ "6666"]
        ]
    }
   ```
<img width="1212" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/bc57b366-603e-4c11-a2b5-198e4a33fd62">

#### 6️⃣ ReplaceNode 
 - 오브젝트가 replaceTargets에 지정한 키를 갖는다면, 해당 키를 replacement에 지정한 값으로 바꾼다.
   ```
   {
        "id" : "6666",
        "nodeType" : "ReplaceNode",
        "replaceTargets" : ["humidity", "temperature", "co2", "tvoc"], 
        "replacement" : "value",
        "wires" : [
            ["7777"]
        ]
    }
   ```
<img width="1192" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/357fa948-a6aa-4f54-b132-0e6998fbd86d">

#### 7️⃣ SwitchNode
 - Payload가 targetKeySet에 지정한 key를 모두 가질 경우에만 0번 output port로 전송한다.

   ```
   {
        "id" : "7777",
        "nodeType" : "SwitchNode",
        "targetKeySet" : ["devEui", "branch", "place", "sensorType" ,"value"] ,
        "wires" : [
            ["8888"]
        ]
    }
   ```
<img width="440" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/3750a14f-45af-4147-8a59-a24ddb8000f5">

#### 8️⃣ FilterNode
 - targetStrings에 지정한 key에 해당하는 오브젝트들만 가져와 payload에 넣는다.
   ```
   {
        "id" : "8888",
        "nodeType" : "FilterNode",
        "targetStrings" : ["time", "value"],
        "wires" : [
            ["9999", "10"]
        ]
    }
   ```
<img width="1180" alt="image" src="https://github.com/caboooom/iotGateway/assets/124178635/d26ecad6-855a-431e-b3b1-e9a8a5ce59d8">

#### 9️⃣ MqttOutNode
 - 내부 노드인 ClientNode가 localhost에 메시지를 publish한다. (ClientNode는 외부에서 별도로 생성되어 주입된다.)
   ```
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
   ```

#### 🔟 localhost에서 수신한 해당 데이터들은 influxDB에 저장된다.

<br>

# 클래스 다이어그램
![image](https://github.com/caboooom/iotGateway/assets/124178635/e579ef60-26c1-4981-b055-52078ef6310c)

![image](https://github.com/caboooom/iotGateway/assets/124178635/42fab222-4e0e-4123-9380-f28d2f4c2978)
(수정 예정)


# 사용한 라이브러리 목록

- [Jackson Databind](https://github.com/FasterXML/jackson-databind)
- [Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Log4j API](https://logging.apache.org/log4j/2.x/log4j-api/)
- [Log4j Core](https://logging.apache.org/log4j/2.x/log4j-core/)
- [Eclipse Paho](https://www.eclipse.org/paho/)
- [Project Lombok](https://projectlombok.org/)
- [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/)
- [JUnit Jupiter Engine](https://junit.org/junit5/docs/current/user-guide/)
