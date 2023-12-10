package com.nhnacademy.aiot.modbus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.aiot.Msg;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.util.JSONUtils;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ModbusServerNode extends Node {
    private static final int REGISTERS_SIZE = 10000;
    private static final int THREADPOOL_SIZE = 20;
    private static final byte UNIT_ID = 1;
    private static final int INPUT_BUFFER_SIZE = 1024;
    private static final int FUNCTION_CODE_IDX = 7;
    private static final int LENGTH_IDX = 5;
    private static final int UNIT_ID_IDX = 6;
    private static final int MBAP_HEADER_LENGTH = 7;
    private static final int DEFAULT_PORT = 11502;

    static int[] holdingRegisters = new int[REGISTERS_SIZE];
    static int[] inputRegisters = new int[REGISTERS_SIZE];

    static{
        for (int i = 0; i < holdingRegisters.length; i++) {
            holdingRegisters[i] = i;
            inputRegisters[i] = holdingRegisters.length - i;
        }
    }

    ExecutorService executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
    ServerSocket serverSocket;

    public ModbusServerNode(String id, int outputPortCount) {
        super(id, outputPortCount);
    }

    @Override
    public void preprocess() {
        executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void process() {
        try {
            Socket socket = serverSocket.accept();
            log.debug("connected - " + socket.getRemoteSocketAddress());
            executorService.execute(() -> tcp(socket));
        } catch (IOException e) {
            log.error("connected error - ", e.getMessage());
        }
    }

    private void tcp(Socket socket) {
        try (Socket clientSocket = socket;
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {
            while (socket.isConnected()) {
                byte[] inputBuffer = new byte[INPUT_BUFFER_SIZE];
                int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);
                byte[] mbapHeader = Arrays.copyOfRange(inputBuffer, 0, MBAP_HEADER_LENGTH);

                if (!isValidPacket(receivedLength, inputBuffer)) {
                    break;
                }

                byte[] requestPdu = Arrays.copyOfRange(inputBuffer, 7, receivedLength);
                FunctionCodes function = FunctionCodes.getByCode(inputBuffer[FUNCTION_CODE_IDX]);

                byte[] responsePdu = function.getFunction().apply(requestPdu);

                byte[] modbusResponse = new byte[mbapHeader.length + responsePdu.length];
 
                // 인풋으로 받은 헤더를 modbusResponse에 복사
                System.arraycopy(mbapHeader, 0, modbusResponse, 0, mbapHeader.length);
                // 요청에 응답할 PDU를 modbusResponse의 MBAP 헤더 뒤에 복사
                System.arraycopy(responsePdu, 0, modbusResponse, 7, responsePdu.length);

                modbusResponse[LENGTH_IDX] = (byte) (responsePdu.length + 1);
 
                ObjectNode jsonNode = JSONUtils.getMapper().createObjectNode();
                jsonNode.put("ModbusResponse", Arrays.toString(modbusResponse));

                out(new Msg("aaaaa", jsonNode));
                outputStream.write(modbusResponse);
                outputStream.flush();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isValidPacket(int receivedLength, byte[] inputBuffer) {
        if (receivedLength < 0 || UNIT_ID != inputBuffer[UNIT_ID_IDX]) {
            return false;
        }

        if (!((receivedLength > 7) && (6 + inputBuffer[LENGTH_IDX]) == receivedLength)) {
            log.info("수신 패킷 길이가 잘못되었습니다.");
            return false;
        }

        return true;
    }



}
