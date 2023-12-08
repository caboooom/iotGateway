package com.nhnacademy.iot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ModbusServer {
 static int[] holdingRegisters = new int[100];

    static {
     for (int i = 0; i < holdingRegisters.length; i++) {
         holdingRegisters[i] = i;
        }
    }
    
   
    public static void main(String[] args) {
        byte unitId = 0;
        

        for (int i = 0; i < holdingRegisters.length; i++) {
            holdingRegisters[i] = i;
        }

        try (ServerSocket serverSocket = new ServerSocket(11502)) {
            try (Socket socket = serverSocket.accept()) {
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());

                while (socket.isConnected()) {
                    byte[] inputBuffer = new byte[1024];
                    int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);

                    if (receivedLength < 0)
                        break; // 수신한 길이가 0보다 작으면 멈춤

                    System.out.println( "Request - " +  Arrays.toString(Arrays.copyOfRange(inputBuffer, 0, receivedLength))); // 들어온 버퍼를 길이만큼 카피해서 출력

                    if (!((receivedLength > 7) && (6 + inputBuffer[5]) == receivedLength)) {
                        System.err.println("수신 패킷 길이가 잘못되었습니다.");
                    }
                    if (unitId != inputBuffer[6]) {
                        break;
                    }
                    int transactionId = (inputBuffer[0] << 8) | inputBuffer[1];

                    int functionCode = inputBuffer[7];
 
                    byte[] requestPdu = Arrays.copyOfRange(inputBuffer, 7, receivedLength);
                    //System.out.println("pdu  - " + Arrays.toString(requestPdu));
                    
                    FunctionCodes function = FunctionCodes.getByCode(functionCode); 
                    
                    byte[] responsePdu = function.getFunction().apply(requestPdu);
                    System.out.println("Response : "+ Arrays.toString(responsePdu));

                    // 여기다가 헤더 더해서 보내는 코드 만들어야 함

                    break;
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



}
