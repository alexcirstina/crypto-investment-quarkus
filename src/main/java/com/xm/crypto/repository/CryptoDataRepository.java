package com.xm.crypto.repository;

import com.xm.crypto.entity.CryptoData;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Singleton
public class CryptoDataRepository implements PanacheRepository<CryptoData> {

    @CacheResult(cacheName = "crypto-data-cache")
    public List<CryptoData> findBySymbol(String symbol){
        List<CryptoData> cryptoDataList = list("symbol", symbol).stream().toList();
        return cryptoDataList.isEmpty() ? null : cryptoDataList ;
    }

    @CacheResult(cacheName = "crypto-data-cache")
    public List<CryptoData> findBySymbolAndDay(String symbol, String date){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDayTimestamp = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endOfDayTimestamp = LocalDateTime.of(localDate, LocalTime.MAX);
        return list("symbol = ?1 AND timestamp >= ?2 AND timestamp <= ?3", symbol, startOfDayTimestamp, endOfDayTimestamp).stream().toList();
    }

    @CacheResult(cacheName = "crypto-data-cache")
    public List<String> findAllDistinctSymbols(){
        return find("SELECT c.symbol FROM CryptoData c group by c.symbol").project(String.class).stream().toList();
    }

}
