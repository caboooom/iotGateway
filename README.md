쓰는즁

LoRa 디바이스를 관리하는 Chirpstack 서버와 연동하여 센서 데이터를 수집하는 Gateway를 구현하였습니다. 
<br>

# 목차
 - 개요
 - 전체 flow
 - 사용한 라이브러리 목록
 <br>

# 개요

 - 프로젝트 이름 : IoT GateWay
 - 프로젝트 기간 : 2023.11.28 - 2023.12.
 - 개발 언어 : JAVA
 - 개발 환경 : VSCODE
 - 멤버 : 박상진, 신민석, 유창은, 임세연
 <br>



# 전체 flow

 - 연동 서버 : tcp://ems.nhnacademy.com:1883

 - 메시징 프로토콜: MQTT

![Flow예시수정예정](https://github.com/caboooom/iotGateway/assets/124178635/2be194f9-d2ff-447b-ad99-2ce5f8ed58e0)
(이미지는 수정할 예정)


#### Config.java
 - Command line argument 또는 별도 설정파일로 옵션을 지정했을 경우, 이를 처리한다.

 - Command line argument 옵션
      - --an : application name을 설정 가능하며, 설정할 경우 해당 메시지만 수신한다.
        
      - -s : 허용 가능한 센서 종류를 지정할 수 있다. 센서 종류가 2개 이상일 경우 ','로 구분한다.
      - 옵션 지정 예시
        ```
        --an myApplicationName123 -s temperature,humidity,co2
        ```

 - 별도의 설정파일
      - 형식은 JSON 형식으로 제한한다.
        
      - Command line argument 옵션과 마찬가지로, applicationName과 센서 종류를 지정할 수 있다.
      - 설정파일 예시
      ```
      { "applicationName" : "myApplicationName123",
         "sensors" :  ["temperature", "humidity", "co2"] }
      ```

#### MqttInNode.java
 - 내부에 clientNode를 가지고 있고, clientNode가 서버로부터 MQTT 메시지를 수신한다.
 - 수신한 데이터를 전처리하여, 다음 노드에게 전달한다.
 
#### SensorTypeFilterNode.java
 - sensorType을 구분하여 데이터를 후처리하고, 다음 노드에게 전달한다.

#### MqttOutNode.java
 - 내부에 clientNode를 가지고 있다.
 - clientNode는 MQTT 메시지를 publish하여 localhost가 수신할 수 있도록 한다.
 - localhost에서 수신한 해당 데이터들은 influxDB에 저장한다.

#### DebugNode.java
 - 입력, 출력, 비정상 처리된 패킷 수를 남긴다.


# 사용한 라이브러리 목록
