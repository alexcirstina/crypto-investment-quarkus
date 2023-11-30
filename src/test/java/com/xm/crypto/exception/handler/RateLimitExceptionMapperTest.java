package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.model.CryptoError;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitExceptionMapperTest {

    private final static String EXCEPTION_MESSAGE = "Invalid input";
    private final RateLimitExceptionMapper rateLimitExceptionMapper = new RateLimitExceptionMapper();

    @Test
    void badRequestExceptionMapperHandlesBadRequestException(){
        Response response = rateLimitExceptionMapper.toResponse(new RateLimitException(EXCEPTION_MESSAGE));
        CryptoError error = response.readEntity(CryptoError.class);
        assertEquals(EXCEPTION_MESSAGE, error.details());
        assertEquals(Response.Status.TOO_MANY_REQUESTS.getStatusCode(), error.statusCode());
    }

}