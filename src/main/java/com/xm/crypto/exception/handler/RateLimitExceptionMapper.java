package com.xm.crypto.exception.handler;

import com.xm.crypto.exception.model.CryptoError;
import io.quarkus.logging.Log;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RateLimitExceptionMapper  implements ExceptionMapper<RateLimitException> {

    @Override
    public Response toResponse(RateLimitException e) {
        Log.info("Surpassed rate limit!", e);
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(new CryptoError(
                Response.Status.TOO_MANY_REQUESTS.getStatusCode(),
                e.getMessage()
        )).build();
    }

}