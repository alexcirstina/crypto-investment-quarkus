package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.model.CryptoError;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        Log.error("Error caught!", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new CryptoError(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                e.getMessage()
        )).build();
    }

}
