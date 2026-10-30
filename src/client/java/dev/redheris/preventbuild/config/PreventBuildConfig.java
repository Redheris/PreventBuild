package dev.redheris.preventbuild.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.config.gson.IdentifierAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PreventBuildConfig {
    public static final Path CONFIGS_DIR = FabricLoader.getInstance().getConfigDir().resolve("preventbuild");
    public static final Path CONDITIONS_DIR = CONFIGS_DIR.resolve("conditions");
    public static final Path COMMON_CONFIG = CONFIGS_DIR.resolve("preventbuild.json");
    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Identifier.class, new IdentifierAdapter())
            .create();

    public static final Defaults DEFAULTS = new Defaults(true, 3000, false);

    @SerializedName("fix_pick_item_by_block")
    private boolean fixPickItemByBlock = DEFAULTS.fixPickItemByBlock;
    @SerializedName("slot_change_waiting_timeout_ms")
    private int maxWaitingTimeoutMs = DEFAULTS.maxWaitingTimeoutMs;
    @SerializedName("log_slot_change_waiting_time")
    private boolean logWaitingTime = DEFAULTS.logWaitingTime;

    public boolean fixPickItemByBlock() {
        return fixPickItemByBlock;
    }

    public int maxWaitingTimeoutMs() {
        return maxWaitingTimeoutMs;
    }

    public boolean logWaitingTime() {
        return logWaitingTime;
    }

    public void setFixPickItemByBlock(boolean fixPickItemByBlock) {
        this.fixPickItemByBlock = fixPickItemByBlock;
    }

    public void setMaxWaitingTimeoutMs(int maxWaitingTimeoutMs) {
        this.maxWaitingTimeoutMs = maxWaitingTimeoutMs;
    }

    public void setLogWaitingTime(boolean logWaitingTime) {
        this.logWaitingTime = logWaitingTime;
    }

    public boolean read(boolean requestedByUser) {
        ensureConfigDirectoriesExists();

        if (!Files.exists(COMMON_CONFIG)) {
            save();
            return true;
        }

        PreventBuildConfig config;
        try (var reader = new FileReader(COMMON_CONFIG.toFile())) {
            config = gson.fromJson(reader, PreventBuildConfig.class);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Failed to read common config file \"{}\"", COMMON_CONFIG, e);
            return false;
        }

        this.fixPickItemByBlock = config.fixPickItemByBlock;
        this.maxWaitingTimeoutMs = config.maxWaitingTimeoutMs;
        if (requestedByUser) {
            this.logWaitingTime = config.logWaitingTime;
        }

        return true;
    }

    public void save() {
        ensureConfigDirectoriesExists();
        try (var writer = new FileWriter(COMMON_CONFIG.toFile())) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Failed to save common config file \"{}\"", COMMON_CONFIG, e);
        }
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

    public record Defaults(
            boolean fixPickItemByBlock,
            int maxWaitingTimeoutMs,
            boolean logWaitingTime
    ) {}
}
