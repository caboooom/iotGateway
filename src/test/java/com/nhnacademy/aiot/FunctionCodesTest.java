package com.nhnacademy.aiot;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.nhnacademy.aiot.modbus.FunctionCodes;

class FunctionCodesTest {

    @Test
    void testReadHoldingRegisters() {

        byte[] request = { 3, 0, 1, 0, 2 };
        FunctionCodes functionCode = FunctionCodes.READ_HOLDING_REGISTERS;

        byte[] expectedResponse = { 3, 4, 0, 1, 0, 2 };

        byte[] actualResponse = functionCode.getFunction().apply(request);

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    void testWriteSingleHoldingRegister() {

        byte[] request = { 6, 0, 1, 0, 3 };
        FunctionCodes functionCode = FunctionCodes.WRITE_SINGLE_HOLDING_REGISTER;

        byte[] expectedResponse = { 6, 0, 1, 0, 3 };

        byte[] actualResponse = functionCode.getFunction().apply(request);

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetByCode() {

        int code = 0x06;
        FunctionCodes functionCode = FunctionCodes.getByCode(code);

        assertEquals(FunctionCodes.WRITE_SINGLE_HOLDING_REGISTER, functionCode);
    }
}