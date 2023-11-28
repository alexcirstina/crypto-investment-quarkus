package com.xm.crypto.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUtilTest {

    private final Path path = mock(Path.class);
    private final BasicFileAttributes attributes = mock(BasicFileAttributes.class);

    @Test
    void returnsInstantOfFile() throws IOException {
        Instant instant = Instant.now();
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            when(attributes.lastModifiedTime()).thenReturn(FileTime.from(instant));
            files.when(() -> Files.readAttributes(path, BasicFileAttributes.class)).thenReturn(attributes);
            assertEquals(instant, FileUtil.lastModified(path));
            files.verify(() -> Files.readAttributes(path, BasicFileAttributes.class));
        }
    }

    @Test
    void returnsDefaultOnThrow() throws IOException {
        Instant instant = Instant.now();
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            when(attributes.lastModifiedTime()).thenReturn(FileTime.from(instant));
            files.when(() -> Files.readAttributes(path, BasicFileAttributes.class))
                            .thenThrow(new IOException());
            assertEquals(Instant.MIN, FileUtil.lastModified(path));
            files.verify(() -> Files.readAttributes(path, BasicFileAttributes.class));
        }
    }

    @Test
    void getCryptoPathFromResources(){
        assertNotNull(FileUtil.getCryptoPath("crypto"));
    }

}