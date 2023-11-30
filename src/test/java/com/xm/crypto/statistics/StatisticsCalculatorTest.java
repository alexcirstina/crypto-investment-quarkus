package com.xm.crypto.statistics;

import com.xm.crypto.dto.AmountAndTimeDto;
import com.xm.crypto.entity.CryptoData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsCalculatorTest {

    private static final LocalDateTime OLDEST_TIMESTAMP = LocalDateTime.MIN;
    private static final LocalDateTime NEWEST_TIMESTAMP = LocalDateTime.MAX;
    private static final BigDecimal MIN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal(20);

    private final StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
    private final StatisticsCalculator spyStatsCalculator = spy(statisticsCalculator);

    @Test
    void oldestReturnsOldestData(){
        AmountAndTimeDto oldest = statisticsCalculator.oldest().apply(cryptoDataList());
        assertEquals(OLDEST_TIMESTAMP, oldest.timestamp());
    }

    @Test
    void oldestReturnsNullForInvalidData(){
        AmountAndTimeDto oldest = statisticsCalculator.oldest().apply(List.of());
        assertNull(oldest);
    }

    @Test
    void newestReturnsNewestData(){
        AmountAndTimeDto newest = statisticsCalculator.newest().apply(cryptoDataList());
        assertEquals(NEWEST_TIMESTAMP, newest.timestamp());
    }

    @Test
    void newestReturnsNullForInvalidData(){
        AmountAndTimeDto newest = statisticsCalculator.newest().apply(List.of());
        assertNull(newest);
    }

    @Test
    void minReturnsSmallestAmount(){
        AmountAndTimeDto min = statisticsCalculator.min().apply(cryptoDataList());
        assertEquals(MIN_AMOUNT, min.value());
    }

    @Test
    void minReturnsNullForInvalidData(){
        AmountAndTimeDto min = statisticsCalculator.min().apply(List.of());
        assertNull(min);
    }

    @Test
    void maxReturnsLargestAmount(){
        AmountAndTimeDto max = statisticsCalculator.max().apply(cryptoDataList());
        assertEquals(MAX_AMOUNT, max.value());
    }

    @Test
    void maxReturnsNullForInvalidData(){
        AmountAndTimeDto max = statisticsCalculator.max().apply(List.of());
        assertNull(max);
    }

    @Test
    void normalizedRangeReturnsValidData(){
        Function minFunction = mock(Function.class);
        Function<List<CryptoData>, AmountAndTimeDto> maxFunction = mock(Function.class);
        doReturn(minFunction).when(spyStatsCalculator).min();
        doReturn(maxFunction).when(spyStatsCalculator).max();
        when(minFunction.apply(cryptoDataList())).thenReturn(new AmountAndTimeDto(OLDEST_TIMESTAMP, MIN_AMOUNT));
        when(minFunction.apply(cryptoDataList())).thenReturn(new AmountAndTimeDto(OLDEST_TIMESTAMP, MAX_AMOUNT));
        BigDecimal result = MAX_AMOUNT.subtract(MIN_AMOUNT).divide(MIN_AMOUNT, RoundingMode.HALF_EVEN);
        BigDecimal bigDecimal = statisticsCalculator.normalizedRange().apply(cryptoDataList());
        assertEquals(0, result.compareTo(bigDecimal));
    }

    private List<CryptoData> cryptoDataList(){
        return List.of(
                new CryptoData(1L, OLDEST_TIMESTAMP, "BTC", MIN_AMOUNT),
                new CryptoData(2L, NEWEST_TIMESTAMP, "BTC", MAX_AMOUNT)
        );
    }

}