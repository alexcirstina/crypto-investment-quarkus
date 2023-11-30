package com.xm.crypto.statistics;

import com.xm.crypto.dto.AmountAndTimeDto;
import com.xm.crypto.entity.CryptoData;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

@Singleton
@RequiredArgsConstructor
public class StatisticsCalculator {

    public Function<List<CryptoData>, AmountAndTimeDto> oldest(){
        return cryptoDataList -> Optional.ofNullable(cryptoDataList)
                .stream()
                .flatMap(Collection::stream)
                .min(Comparator.comparing(CryptoData::getTimestamp))
                .map(cryptoData -> new AmountAndTimeDto(cryptoData.getTimestamp(), cryptoData.getPrice()))
                .orElse(null);
    }

    public Function<List<CryptoData>, AmountAndTimeDto> newest(){
        return cryptoDataList -> Optional.ofNullable(cryptoDataList)
                .stream()
                .flatMap(Collection::stream)
                .max(Comparator.comparing(CryptoData::getTimestamp))
                .map(cryptoData -> new AmountAndTimeDto(cryptoData.getTimestamp(), cryptoData.getPrice()))
                .orElse(null);
    }

    public Function<List<CryptoData>, AmountAndTimeDto> min(){
        return cryptoDataList -> Optional.ofNullable(cryptoDataList)
                .stream()
                .flatMap(Collection::stream)
                .min(Comparator.comparing(CryptoData::getPrice))
                .map(cryptoData -> new AmountAndTimeDto(cryptoData.getTimestamp(), cryptoData.getPrice()))
                .orElse(null);
    }

    public Function<List<CryptoData>, AmountAndTimeDto> max(){
        return cryptoDataList -> Optional.ofNullable(cryptoDataList)
                .stream()
                .flatMap(Collection::stream)
                .max(Comparator.comparing(CryptoData::getPrice))
                .map(cryptoData -> new AmountAndTimeDto(cryptoData.getTimestamp(), cryptoData.getPrice()))
                .orElse(null);
    }

    public Function<List<CryptoData>, BigDecimal> normalizedRange(){
        return cryptoDataList -> Optional.ofNullable(cryptoDataList)
                .map(cryptoData ->
                        max().apply(cryptoData).value().subtract(min().apply(cryptoData).value())
                                .divide(min().apply(cryptoData).value(),  RoundingMode.HALF_EVEN)
                ).orElse(null);
    }



}
