package dev.redheris.preventbuild.config;

import dev.redheris.preventbuild.PreventBuild;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PreventBuildConfig {
    public static final Path CONFIGS_DIR = FabricLoader.getInstance().getConfigDir().resolve("preventbuild");
    public static final Path CONDITIONS_DIR = CONFIGS_DIR.resolve("conditions");

    public void read() {
        ensureConfigDirectoriesExists();
    }

    public void save() {

    }

    public static void ensureConfigDirectoriesExists() {
        if (!Files.exists(CONDITIONS_DIR)) {
            try {
                Files.createDirectories(CONDITIONS_DIR);
            } catch (IOException e) {
                PreventBuild.LOGGER.error("Exception during creating config directories: {}", CONDITIONS_DIR);
                throw new RuntimeException("Exception during creating config directories", e);
            }
        }
    }
}
