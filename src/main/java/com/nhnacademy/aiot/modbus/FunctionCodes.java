package com.nhnacademy.aiot.modbus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public enum FunctionCodes {

    READ_HOLDING_REGISTERS(0x03, request -> {

        int address = concatBytes(request[1], request[2]);
        int quantity = concatBytes(request[3], request[4]);

        byte[] pdu = new byte[1 + 1 + (quantity * 2)];

        pdu[0] = 3;
        pdu[1] = (byte) (quantity * 2);

        for (int i = 0; i < quantity; i++) {
            pdu[2 + (i * 2)] = (byte) ((ModbusServerNode.holdingRegisters[address] >> 8) & 0xFF);
            pdu[3 + (i * 2)] = (byte) (ModbusServerNode.holdingRegisters[address++] & 0xFF);
        }
        return pdu;
    }),

    READ_INPUT_REGISTERS(0x04, request -> {
        int address = concatBytes(request[1], request[2]);
        int quantity = concatBytes(request[3], request[4]);

        byte[] pdu = new byte[1 + 1 + (quantity * 2)];

        pdu[0] = 3;
        pdu[1] = (byte) (quantity * 2);

        for (int i = 0; i < quantity; i++) {
            pdu[2 + (i * 2)] = (byte) ((ModbusServerNode.inputRegisters[address] >> 8) & 0xFF);
            pdu[3 + (i * 2)] = (byte) (ModbusServerNode.inputRegisters[address++] & 0xFF);
        }
        return pdu;
    }),  
    
    WRITE_SINGLE_HOLDING_REGISTER(0x06, request -> {
        int address = concatBytes(request[1], request[2]);
        int value = concatBytes(request[3], request[4]);
    
        ModbusServerNode.holdingRegisters[address] = value;
    
        byte[] responsePdu = new byte[5];
    
        responsePdu[0] = 0x06;
    
        responsePdu[1] = request[1];
        responsePdu[2] = request[2];
    
        responsePdu[3] = request[3];
        responsePdu[4] = request[4];
    
        return responsePdu;
    }),
    
    WRITE_MULTIPLE_HOLDING_REGISTERS(0x10, request -> {

        int address = concatBytes(request[1], request[2]);
        int quantity = concatBytes(request[3], request[4]);
        int byteCount = (request[5]);

        if (byteCount != (quantity * 2)) {
            byte[] errorResponse = new byte[1];
            return errorResponse;
        }

        for (int i = address, j = 6; i < address + quantity; i++, j += 2) {
            ModbusServer.holdingRegisters[address] = concatBytes(request[j], request[j + 1]);
        }

        byte[] responsePdu = new byte[5];

        // Function code
        responsePdu[0] = 0x10;

        // Starting Address
        responsePdu[1] = request[1];
        responsePdu[2] = request[2];

        // quantity
        responsePdu[3] = request[3];
        responsePdu[4] = request[4];

        return responsePdu;
    });

    private final int code;
    private final UnaryOperator<byte[]> function;
    private static final Map<Integer, FunctionCodes> codeMap = new HashMap<>();

    static {
        for (FunctionCodes func : FunctionCodes.values()) {
            codeMap.put(func.code, func);
        }
    }

    private static int concatBytes(byte firstByte, byte secondByte) {
        return ((firstByte & 0xFF) << 8) | (secondByte & 0xFF);
    }

    FunctionCodes(int code, UnaryOperator<byte[]> function) {
        this.code = code;
        this.function = function;
    }

    public int getCode() {
        return code;

    }

    public UnaryOperator<byte[]> getFunction() {
        return function;
    }

    public static FunctionCodes getByCode(int code) {
        // 널포인트 처리
        return codeMap.get(code);
    }

}
