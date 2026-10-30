package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.config.PreventBuildConfig;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static dev.redheris.preventbuild.config.PreventBuildConfig.gson;

public class ActiveConfigsStorage {
    public static final Path STORAGE_FILE_PATH = PreventBuildConfig.CONFIGS_DIR.resolve("active_configs.json");

    public static Set<String> read() throws IOException {
        PreventBuildConfig.ensureConfigDirectoriesExists();
        if (Files.exists(STORAGE_FILE_PATH)) {
            try (var reader = new FileReader(STORAGE_FILE_PATH.toFile())) {
                String[] values = gson.fromJson(reader, String[].class);
                return Set.of(values);
            }
        }
        return Set.of();
    }

    public static void save(Set<String> activeConfigNames) throws IOException {
        try (var writer = new FileWriter(STORAGE_FILE_PATH.toFile())) {
            gson.toJson(activeConfigNames, writer);
        }
    }
}
