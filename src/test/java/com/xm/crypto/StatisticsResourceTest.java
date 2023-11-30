package com.xm.crypto;

import com.xm.crypto.statistics.StatisticsService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StatisticsResourceTest {

    private static final String CRYPTO_SYMBOL = "BTC";
    private static final String VALID_DATE = "2022-01-22";
    private final StatisticsService statisticsService = mock(StatisticsService.class);
    private final StatisticsResource statisticsResource = new StatisticsResource(statisticsService);

    @Test
    void generalStatsCallsService(){
        statisticsResource.generalStats(CRYPTO_SYMBOL);
        verify(statisticsService).generalStats(CRYPTO_SYMBOL);
    }

    @Test
    void normalizedRangeCallsService(){
        statisticsResource.normalizedRange();
        verify(statisticsService).normalizedRangeDesc(null);
    }

    @Test
    void normalizedRangeWithDateCallsService(){
        statisticsResource.normalizedRangeWithDate(VALID_DATE);
        verify(statisticsService).normalizedRangeDesc(VALID_DATE);
    }
}