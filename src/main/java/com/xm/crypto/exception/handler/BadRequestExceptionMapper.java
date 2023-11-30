package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.BadRequestException;
import com.xm.crypto.exception.model.CryptoError;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException e) {
        Log.info("Bad request!", e);
        return Response.status(Response.Status.BAD_REQUEST).entity(new CryptoError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                e.getMessage()
        )).build();
    }

}
