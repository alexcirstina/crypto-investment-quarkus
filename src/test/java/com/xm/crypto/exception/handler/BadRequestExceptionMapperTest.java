package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.BadRequestException;
import com.xm.crypto.exception.model.CryptoError;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionMapperTest {

    private final static String EXCEPTION_MESSAGE = "Invalid input";
    private final BadRequestExceptionMapper badRequestExceptionMapper = new BadRequestExceptionMapper();

    @Test
    void badRequestExceptionMapperHandlesBadRequestException(){
        Response response = badRequestExceptionMapper.toResponse(new BadRequestException(EXCEPTION_MESSAGE));
        CryptoError error = response.readEntity(CryptoError.class);
        assertEquals(EXCEPTION_MESSAGE, error.details());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), error.statusCode());
    }
}