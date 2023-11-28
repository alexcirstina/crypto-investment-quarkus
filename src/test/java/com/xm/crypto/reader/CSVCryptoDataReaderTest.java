package com.xm.crypto.reader;

import com.xm.crypto.entity.Crypto;
import com.xm.crypto.exception.CryptoDataReaderException;
import com.xm.crypto.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVCryptoDataReaderTest {

    private final String PATH = "crypto";
    private final CSVCryptoDataReader csvCryptoDataReader = new CSVCryptoDataReader();
    private final CSVCryptoDataReader csvSpy = spy(csvCryptoDataReader);
    private final URL url = mock(URL.class);
    private final URI uri = mock(URI.class);
    private final Path path = mock(Path.class);

    @Test
    void findAllReturnsMapOfCrypto() throws URISyntaxException {
        try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
            fileUtil.when(() -> FileUtil.getCryptoPath(PATH)).thenReturn(url);
            try (MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class)) {
                when(url.toURI()).thenReturn(uri);
                paths.when(() -> Paths.get(uri)).thenReturn(path);
                doReturn(Map.of("BTC", new Crypto("", List.of()))).when(csvSpy).readAllFiles(path);
                Map<String, Crypto> cryptoMap = csvSpy.findAll();
                assertNotNull(cryptoMap.get("BTC"));
            }
        }
    }

    @Test
    void findAllReturnsEmptyMapOnNullPath() throws URISyntaxException {
        try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
            fileUtil.when(() -> FileUtil.getCryptoPath(PATH)).thenReturn(null);
            Map<String, Crypto> cryptoMap = csvSpy.findAll();
            assertTrue(cryptoMap.isEmpty());
        }
    }

    @Test
    void findAllThrowsExceptionOnInvalidPath() throws URISyntaxException {
        try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
            fileUtil.when(() -> FileUtil.getCryptoPath(PATH)).thenReturn(url);
            when(url.toURI()).thenThrow(new URISyntaxException("", ""));
            assertThrows(CryptoDataReaderException.class, () -> csvSpy.findAll());
        }
    }

    @Test
    void readAllFilesFromFolder(){
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            Stream<Path> pathStream = Stream.of(path);
            files.when(() -> Files.walk(path)).thenReturn(pathStream);
            try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
                when(path.toString()).thenReturn("Something.csv");
                files.when(() -> Files.isRegularFile(path)).thenReturn(true);
                fileUtil.when(() -> FileUtil.lastModified(path)).thenReturn(Instant.now());
                doReturn(new Crypto("BTC", List.of())).when(csvSpy).csvToObject(path);
                Map<String, Crypto> cryptoMap = csvSpy.readAllFiles(path);
                assertNotNull(cryptoMap.get("BTC"));
            }
        }
    }

    @Test
    void readAllFilesThrowsExceptionForIOException(){
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            files.when(() -> Files.walk(path)).thenThrow(new IOException());
            assertThrows(CryptoDataReaderException.class, () -> csvSpy.readAllFiles(path));
        }
    }

    @Test
    void csvToCryptoMapsCorrectly(){
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            files.when(() -> Files.readAllLines(path)).thenReturn(validCryptoCSV());
            Crypto crypto = csvSpy.csvToObject(path);
            assertSame(2, crypto.cryptoData().size());
        }
    }

    @Test
    void csvToCryptoThrowsExceptionForIOException(){
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            files.when(() -> Files.readAllLines(path)).thenThrow(new IOException());
            assertNull(csvSpy.csvToObject(path));
        }
    }

    private List<String> validCryptoCSV(){
        return List.of(
                "timestamp,symbol,price",
                "1641009600000,BTC,46813.21",
                "1641020400000,BTC,46979.61"
        );
    }


}