package com.xm.crypto.statistics;

import com.xm.crypto.dto.NormalizedRangeDto;
import com.xm.crypto.dto.StatisticsDto;
import com.xm.crypto.entity.CryptoData;
import com.xm.crypto.exception.BadRequestException;
import com.xm.crypto.repository.CryptoDataRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class CryptoStatisticsService implements StatisticsService {

    private final StatisticsCalculator statisticsCalculator;
    private final CryptoDataRepository cryptoDataRepository;

    public StatisticsDto generalStats(String symbol) {
        List<CryptoData> cryptoDataList = cryptoDataRepository.findBySymbol(symbol);

        return Optional.ofNullable(cryptoDataList)
                .map(cryptoDataL ->
                        new StatisticsDto(
                                symbol,
                                statisticsCalculator.oldest().apply(cryptoDataL),
                                statisticsCalculator.newest().apply(cryptoDataL),
                                statisticsCalculator.min().apply(cryptoDataL),
                                statisticsCalculator.max().apply(cryptoDataL)
                        )
                ).orElseThrow(() -> new BadRequestException("Invalid crypto symbol provided!"));
    }


    public List<NormalizedRangeDto> normalizedRangeDesc(String date) {
        List<String> symbols = cryptoDataRepository.findAllDistinctSymbols();
        return symbols.stream()
                .map(symbol -> getCryptoDataBySymbolAndDay(symbol, date))
                .map(cryptoData -> new NormalizedRangeDto(
                        cryptoData.get(0).getSymbol(),
                        statisticsCalculator.normalizedRange().apply(cryptoData)))
                .sorted((o1, o2) -> o2.normalizedRange().compareTo(o1.normalizedRange()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected List<CryptoData> getCryptoDataBySymbolAndDay(String symbol, String date){
        List<CryptoData> cryptoDataList = Optional.ofNullable(date)
                .map(d -> cryptoDataRepository.findBySymbolAndDay(symbol, d))
                .orElse(cryptoDataRepository.findBySymbol(symbol));
        if(cryptoDataList == null || cryptoDataList.isEmpty()){
            throw new BadRequestException("No data found for provided input!");
        }
        return cryptoDataList;
    }

}
