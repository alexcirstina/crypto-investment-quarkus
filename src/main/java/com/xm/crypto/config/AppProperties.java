package com.xm.crypto.config;

import jakarta.inject.Singleton;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
@Getter
public class AppProperties {

    @ConfigProperty(name = "csv.folder.path")
    String csvFolderPath;

}
