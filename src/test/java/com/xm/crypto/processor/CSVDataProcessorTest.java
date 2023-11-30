package com.xm.crypto.processor;

import com.xm.crypto.config.AppProperties;
import com.xm.crypto.entity.CryptoData;
import com.xm.crypto.exception.CryptoDataReaderException;
import com.xm.crypto.repository.CryptoDataRepository;
import com.xm.crypto.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVDataProcessorTest {

    private final CryptoDataRepository cryptoDataRepository = mock(CryptoDataRepository.class);
    private final AppProperties appProperties = mock(AppProperties.class);
    private final CSVDataProcessor cryptoDataProcessor = new CSVDataProcessor(cryptoDataRepository, appProperties);
    private final CSVDataProcessor csvSpy = spy(cryptoDataProcessor);
    private final Path path = mock(Path.class);

    @Test
    void processAllFilesFromFolder() {
        when(appProperties.getCsvFolderPath()).thenReturn("/path");
        try (MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class)) {
            try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
                paths.when(() -> Paths.get(anyString())).thenReturn(path);
                Stream<Path> pathStream = Stream.of(path);
                files.when(() -> Files.walk(path)).thenReturn(pathStream);
                try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
                    when(path.toString()).thenReturn("Something.csv");
                    doNothing().when(cryptoDataRepository).persist((CryptoData) any());
                    files.when(() -> Files.isRegularFile(path)).thenReturn(true);
                    fileUtil.when(() -> FileUtil.lastModified(path)).thenReturn(Instant.now());
                    doReturn(List.of(new CryptoData(1L, LocalDateTime.now(), "BTC", BigDecimal.valueOf(22)))).when(csvSpy).readFromCSV(any());
                    csvSpy.process();
                    verify(cryptoDataRepository).persist(anyIterable());
                }
            }
        }
    }

    @Test
    void processThrowsReadException() {
        when(appProperties.getCsvFolderPath()).thenReturn("/path");
        try (MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class)) {
            try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
                paths.when(() -> Paths.get(anyString())).thenReturn(path);
                files.when(() -> Files.walk(path)).thenThrow(new IOException());
                assertThrows(CryptoDataReaderException.class, () ->  csvSpy.process());
            }
        }
    }

    @Test
    void csvToCryptoMapsCorrectly() {
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            files.when(() -> Files.readAllLines(path)).thenReturn(validCryptoCSV());
            List<CryptoData> cryptoData = csvSpy.readFromCSV(path);
            assertSame(2, cryptoData.size());
        }
    }

    @Test
    void csvToCryptoThrowsExceptionForIOException() {
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            files.when(() -> Files.readAllLines(path)).thenThrow(new IOException());
            assertEquals(0, csvSpy.readFromCSV(path).size());
        }
    }

    private List<String> validCryptoCSV() {
        return List.of(
                "timestamp,symbol,price",
                "1641009600000,BTC,46813.21",
                "1641020400000,BTC,46979.61"
        );
    }
}