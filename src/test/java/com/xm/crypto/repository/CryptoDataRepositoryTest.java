package com.xm.crypto.repository;

import com.xm.crypto.entity.CryptoData;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class CryptoDataRepositoryTest {

    private static final String CRYPTO_SYMBOL = "BTC";
    private static final String INVALID_CRYPTO_SYMBOL = "LOL";
    private static final String DATE = "2022-01-22";
    private static final String INVALID_DATE = "2019-01-22";
    @Inject CryptoDataRepository cryptoDataRepository;

    @Test
    void findBySymbolReturnsData(){
        List<CryptoData> data =  cryptoDataRepository.findBySymbol(CRYPTO_SYMBOL);
        assertFalse(data.isEmpty());
    }

    @Test
    void findBySymbolReturnsNullForNotFound(){
        List<CryptoData> data =  cryptoDataRepository.findBySymbol(INVALID_CRYPTO_SYMBOL);
        assertNull(data);
    }

    @Test
    void findBySymbolAndDayReturnsData(){
        List<CryptoData> data =  cryptoDataRepository.findBySymbolAndDay(CRYPTO_SYMBOL, DATE);
        assertFalse(data.isEmpty());
    }

    @Test
    void findBySymbolAndDayReturnsNullForNotFound(){
        List<CryptoData> data =  cryptoDataRepository.findBySymbolAndDay(INVALID_CRYPTO_SYMBOL, INVALID_DATE);
        assertTrue(data.isEmpty());
    }

    @Test
    void findAllDistinctSymbolsReturnsAllSymbols(){
        List<String> data =  cryptoDataRepository.findAllDistinctSymbols();
        assertSame(5, data.size());
    }

}