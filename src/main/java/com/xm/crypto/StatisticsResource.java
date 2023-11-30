package com.xm.crypto;

import com.xm.crypto.statistics.StatisticsService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Path("/stats")
public class StatisticsResource {

    private final StatisticsService statisticsService;


    @APIResponses(
        value = {
                @APIResponse(
                    responseCode = "200",
                    description = "Crypto general statistics",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
                ),
                @APIResponse(
                    responseCode = "400",
                    description = "Invalid crypto symbol provided", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                ),
                @APIResponse(
                        responseCode = "429",
                        description = "Surpassed rate limit", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                ),
                @APIResponse(
                        responseCode = "500",
                        description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                )
        }
    )
    @RateLimit(value = 15, window = 2, windowUnit = ChronoUnit.MINUTES)
    @GET
    @Path("/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generalStats(@PathParam("symbol") String symbol){
        return Response.ok().entity(statisticsService.generalStats(symbol)).build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Desc raking of all stored crypto based on normalized range",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "No data found for provided input", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "429",
                            description = "Surpassed rate limit", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    )
            }
    )
    @RateLimit(value = 15, window = 2, windowUnit = ChronoUnit.MINUTES)
    @GET
    @Path("/normalized-range-desc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response normalizedRange(){
        return Response.ok().entity(statisticsService.normalizedRangeDesc(null)).build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Desc raking of all stored crypto based on normalized range for a specified day",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "No data found for provided input", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "429",
                            description = "Surpassed rate limit", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    )
            }
    )
    @RateLimit(value = 15, window = 2, windowUnit = ChronoUnit.MINUTES)
    @GET
    @Path("/normalized-range-desc/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response normalizedRangeWithDate(@PathParam("date") @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2})", message="Invalid date format. Accepted format: yyyy-MM-dd") String date){
        return Response.ok().entity(statisticsService.normalizedRangeDesc(date)).build();
    }

}
