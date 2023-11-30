package com.xm.crypto.statistics;

import com.xm.crypto.dto.AmountAndTimeDto;
import com.xm.crypto.dto.NormalizedRangeDto;
import com.xm.crypto.dto.StatisticsDto;
import com.xm.crypto.entity.CryptoData;
import com.xm.crypto.exception.BadRequestException;
import com.xm.crypto.repository.CryptoDataRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CryptoStatisticsServiceTest {

    private static final String CRYPTO_SYMBOL = "BTC";
    private static final String VALID_DATE = "2022-01-22";
    private static final LocalDateTime OLDEST_TIMESTAMP = LocalDateTime.MIN;
    private static final LocalDateTime RANDOM_TIMESTAMP = LocalDateTime.now();;
    private static final LocalDateTime NEWEST_TIMESTAMP = LocalDateTime.MAX;;
    private static final BigDecimal MIN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal RANDOM_AMOUNT = new BigDecimal(5);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal(20);

    private final StatisticsCalculator statisticsCalculator = mock(StatisticsCalculator.class);
    private final CryptoDataRepository cryptoDataRepository = mock(CryptoDataRepository.class);
    private final CryptoStatisticsService cryptoStatisticsService = new CryptoStatisticsService(statisticsCalculator, cryptoDataRepository);
    private final CryptoStatisticsService statsServiceSpy = spy(cryptoStatisticsService);

    Function oldestFunction = mock(Function.class);
    Function newestFunction = mock(Function.class);
    Function minFunction = mock(Function.class);
    Function maxFunction = mock(Function.class);
    Function normalizedRange = mock(Function.class);

    @Test
    void generalStatsReturnsValidData(){
        List<CryptoData> cryptoData = cryptoDataList();
        when(cryptoDataRepository.findBySymbol(CRYPTO_SYMBOL)).thenReturn(cryptoData);
        when(statisticsCalculator.min()).thenReturn(minFunction);
        when(statisticsCalculator.max()).thenReturn(maxFunction);
        when(statisticsCalculator.oldest()).thenReturn(oldestFunction);
        when(statisticsCalculator.newest()).thenReturn(newestFunction);
        when(minFunction.apply(cryptoData)).thenReturn(new AmountAndTimeDto(RANDOM_TIMESTAMP, MIN_AMOUNT));
        when(maxFunction.apply(cryptoData)).thenReturn(new AmountAndTimeDto(RANDOM_TIMESTAMP, MAX_AMOUNT));
        when(oldestFunction.apply(cryptoData)).thenReturn(new AmountAndTimeDto(OLDEST_TIMESTAMP, RANDOM_AMOUNT));
        when(newestFunction.apply(cryptoData)).thenReturn(new AmountAndTimeDto(NEWEST_TIMESTAMP, RANDOM_AMOUNT));

        StatisticsDto statisticsDto = cryptoStatisticsService.generalStats(CRYPTO_SYMBOL);
        assertEquals(MIN_AMOUNT, statisticsDto.min().value());
        assertEquals(MAX_AMOUNT, statisticsDto.max().value());
        assertEquals(OLDEST_TIMESTAMP, statisticsDto.oldest().timestamp());
        assertEquals(NEWEST_TIMESTAMP, statisticsDto.newest().timestamp());
    }

    @Test
    void generalStatsThrowsExceptionForSymbolNotFound(){
        when(cryptoDataRepository.findBySymbol(CRYPTO_SYMBOL)).thenReturn(null);
        assertThrows(BadRequestException.class, () ->  cryptoStatisticsService.generalStats(CRYPTO_SYMBOL));
    }

    @Test
    void returnNormalizedRangeForAllSymbolsSuccessfully(){
        when(cryptoDataRepository.findAllDistinctSymbols()).thenReturn(List.of("BTC", "ETH"));
        doReturn(cryptoDataList()).when(statsServiceSpy).getCryptoDataBySymbolAndDay("BTC", null);
        doReturn(cryptoDataListETH()).when(statsServiceSpy).getCryptoDataBySymbolAndDay("ETH", null);
        when(statisticsCalculator.normalizedRange()).thenReturn(normalizedRange);
        when(normalizedRange.apply(cryptoDataList())).thenReturn(new BigDecimal(11));
        when(normalizedRange.apply(cryptoDataListETH())).thenReturn(new BigDecimal(22));

        List<NormalizedRangeDto> normalizedRangeDto = statsServiceSpy.normalizedRangeDesc(null);

        assertEquals("ETH", normalizedRangeDto.get(0).symbol());
        assertEquals("BTC", normalizedRangeDto.get(1).symbol());
    }

    @Test
    void getCryptoDataBySymbolAndDayWithNullDay(){
        List<CryptoData> cryptoData = cryptoDataList();
        when(cryptoDataRepository.findBySymbol(CRYPTO_SYMBOL)).thenReturn(cryptoDataList());
        List<CryptoData> cryptoDataList = cryptoStatisticsService.getCryptoDataBySymbolAndDay(CRYPTO_SYMBOL, null);
        assertEquals(cryptoData, cryptoDataList);
    }

    @Test
    void getCryptoDataBySymbolAndDayWithDay(){
        List<CryptoData> cryptoData = cryptoDataList();
        when(cryptoDataRepository.findBySymbolAndDay(CRYPTO_SYMBOL, VALID_DATE)).thenReturn(cryptoData);
        List<CryptoData> cryptoDataList = cryptoStatisticsService.getCryptoDataBySymbolAndDay(CRYPTO_SYMBOL, VALID_DATE);
        assertEquals(cryptoData, cryptoDataList);
    }

    @Test
    void getCryptoDataBySymbolAndDayWithDayThrowsExceptionForNoData(){
        when(cryptoDataRepository.findBySymbolAndDay(CRYPTO_SYMBOL, VALID_DATE)).thenReturn(List.of());
        assertThrows(BadRequestException.class, () ->  cryptoStatisticsService.getCryptoDataBySymbolAndDay(CRYPTO_SYMBOL, VALID_DATE));
    }

    private List<CryptoData> cryptoDataList(){
        return List.of(
                new CryptoData(1L, OLDEST_TIMESTAMP, "BTC", MIN_AMOUNT),
                new CryptoData(2L, NEWEST_TIMESTAMP, "BTC", MAX_AMOUNT)
        );
    }

    private List<CryptoData> cryptoDataListETH(){
        return List.of(
                new CryptoData(1L, OLDEST_TIMESTAMP, "ETH", MIN_AMOUNT),
                new CryptoData(2L, NEWEST_TIMESTAMP, "ETH", MAX_AMOUNT)
        );
    }
}