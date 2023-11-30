package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.model.CryptoError;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralExceptionMapperTest {

    private final static String EXCEPTION_MESSAGE = "Invalid input";
    private final GeneralExceptionMapper generalExceptionMapper = new GeneralExceptionMapper();

    @Test
    void badRequestExceptionMapperHandlesBadRequestException(){
        Response response = generalExceptionMapper.toResponse(new Exception(EXCEPTION_MESSAGE));
        CryptoError error = response.readEntity(CryptoError.class);
        assertEquals(EXCEPTION_MESSAGE, error.details());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), error.statusCode());
    }

}