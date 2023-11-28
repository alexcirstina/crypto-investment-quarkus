package com.xm.crypto.util;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

@Singleton
public final class FileUtil {

    private FileUtil(){}

    public static Instant lastModified(Path path){
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            return attr.lastModifiedTime().toInstant();
        } catch (IOException e) {
            Log.warnf("Could not read lastModified for filename: %s", path.getFileName());
        }
        return Instant.MIN;
    }

    public static URL getCryptoPath(String path){
       return FileUtil.class.getClassLoader().getResource(path);
    }

}
