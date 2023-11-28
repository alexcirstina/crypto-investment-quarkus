package com.xm.crypto.reader;

import com.xm.crypto.entity.Crypto;
import com.xm.crypto.entity.CryptoData;
import com.xm.crypto.util.FileUtil;
import com.xm.crypto.exception.CryptoDataReaderException;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor
public class CSVCryptoDataReader implements CryptoDataReader {


    public static final String CRYPTO_RESOURCE_FOLDER = "crypto";
    public static final String FILE_EXTENSION = ".csv";
    private Instant lastRead = Instant.MIN;

    @Override
    public Map<String, Crypto> findAll() {
        //lastRead = Instant.now();
        URL url = FileUtil.getCryptoPath(CRYPTO_RESOURCE_FOLDER);
        try {
            if (url != null) {
                Path folder = Paths.get(url.toURI());
                return readAllFiles(folder);
            }
        } catch (URISyntaxException e) {
            throw new CryptoDataReaderException("URI syntax exception for: " + url, e);
        }
        return Map.of();
    }

    protected Map<String, Crypto> readAllFiles(Path folder) {
        try (Stream<Path> paths = Files.walk(folder)) {

            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                    .filter(path -> FileUtil.lastModified(path).isAfter(lastRead))
                    .map(this::csvToObject)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(
                            Crypto::symbol,
                            crypto -> crypto));

        } catch (IOException e) {
            throw new CryptoDataReaderException("Invalid or missing crypto files!", e);
        }
    }

    protected Crypto csvToObject(Path path) {
        try {
            List<CryptoData> cryptoData = Files.readAllLines(path)
                    .stream().skip(1)
                    .map(line -> line.split(","))
                    .map(data -> new CryptoData(new Timestamp(Long.parseLong(data[0])), data[1], new BigDecimal(data[2])))
                    .toList();

            return Crypto.builder()
                    .symbol(cryptoData.get(0).symbol())
                    .cryptoData(cryptoData)
                    .build();
        } catch (IOException e) {
            Log.errorf(e, "Could not read crypto with filename: %s", path.getFileName());
            return null;
        }
    }
}
