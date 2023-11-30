package com.xm.crypto.processor;

import com.xm.crypto.config.AppProperties;
import com.xm.crypto.entity.CryptoData;
import com.xm.crypto.exception.CryptoDataReaderException;
import com.xm.crypto.repository.CryptoDataRepository;
import com.xm.crypto.util.DateUtil;
import com.xm.crypto.util.FileUtil;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor
public class CSVDataProcessor {

    private final CryptoDataRepository cryptoDataRepository;
    private final AppProperties appProperties;
    public static final String FILE_EXTENSION = ".csv";
    private final Instant lastRead = Instant.MIN;

    @Startup
    @Transactional
    public void process() {
        try (Stream<Path> paths = Files.walk(Paths.get(appProperties.getCsvFolderPath()))) {

            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                    .filter(path -> FileUtil.lastModified(path).isAfter(lastRead))
                    .map(this::readFromCSV)
                    .filter(Objects::nonNull)
                    .forEach(
                            cryptoData -> {
                                cryptoDataRepository.persist(cryptoData);
                                Log.infof("Data read from CSV and stored in Postgres successfully for %s", cryptoData.get(0).getSymbol());
                            });


        } catch (IOException e) {
            throw new CryptoDataReaderException("Invalid or missing crypto files!", e);
        }
    }

    protected List<CryptoData> readFromCSV(Path path) {
        try {
            return Files.readAllLines(path)
                    .stream().skip(1)
                    .map(line -> line.split(","))
                    .map(data -> new CryptoData(null, DateUtil.stringToLocalDateTime(data[0]), data[1], new BigDecimal(data[2])))
                    .toList();
        } catch (IOException e) {
            Log.errorf(e, "Could not read crypto with filename: %s", path.getFileName());
            return List.of();
        }
    }
}
