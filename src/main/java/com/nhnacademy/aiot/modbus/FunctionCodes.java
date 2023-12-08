package com.nhnacademy.iot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public enum FunctionCodes {

    READ_HOLDING_REGISTERS(0x03, request -> { 
        int address = (request[1] << 8) | request[2];
        int quantity = (request[3] << 8) | request[4];

        byte[] pdu = new byte[1+1 + (quantity*2)];

        pdu[0] = 3;
        pdu[1] = (byte) (quantity*2);

        for (int i = 0; i < quantity; i++) {
            pdu[2 + (i * 2)] = (byte) ((ModbusServer.holdingRegisters[address] >> 8) & 0xFF); 
            pdu[3 + (i * 2)] = (byte) (ModbusServer.holdingRegisters[address++] & 0xFF);    
        }
       return pdu;
    }),

    READ_INPUT_REGISTERS(0x04, request -> { return null;}),
    WRITE_SINGLE_HOLDING_REGISTER(0x06, request -> { return null;}),
    WRITE_MULTIPLE_HOLDING_REGISTERS(0x10, request -> { return null;});

    private final int code;
    private final UnaryOperator<byte[]> function;
    private static final Map<Integer, FunctionCodes> codeMap = new HashMap<>();

    static {
        for (FunctionCodes func : FunctionCodes.values()) {
            codeMap.put(func.code, func);
        }
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

    public static FunctionCodes getByCode(int code){
        //널포인트 처리
        return codeMap.get(code);
    }
}