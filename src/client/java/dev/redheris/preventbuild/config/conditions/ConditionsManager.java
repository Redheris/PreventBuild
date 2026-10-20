package dev.redheris.preventbuild.config.conditions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.redheris.preventbuild.PreventBuild;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConditionsManager {
    public static final Path CONFIGS_DIR = FabricLoader.getInstance().getConfigDir().resolve("preventbuild");
    public static final Path CONDITIONS_DIR = CONFIGS_DIR.resolve("conditions");
    private static final Path COMMON_CONFIG = CONFIGS_DIR.resolve("conditions.json");
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private List<ConditionConfig> configs = List.of();

    public List<ConditionConfig> getConfigs() {
        return configs;
    }

    public void setActive(ConditionConfig config, boolean value) {
        config.setActive(value);
        save();
    }

    public void read() {
        initConfigDirs();

        List<ConditionConfig> updated = ConditionConfigsReader.readConditions();
        configs = List.copyOf(updated);

        if (Files.exists(COMMON_CONFIG)) {
            try (var reader = new FileReader(COMMON_CONFIG.toFile())) {
                @SuppressWarnings("unchecked")
                Map<String, Boolean> map = (Map<String, Boolean>) gson.fromJson(reader, Map.class);

                for (ConditionConfig config : configs) {
                    if (map.containsKey(config.getName())) {
                        config.setActive(map.get(config.getName()));
                    }
                }
            } catch (IOException e) {
                PreventBuild.LOGGER.error("Exception during reading common conditions config file");
                throw new RuntimeException(e);
            }
        }
        save();
    }

    public void save() {
        Map<String, Boolean> configsMap = configs.stream()
                .collect(Collectors.toMap(
                        ConditionConfig::getName,
                        ConditionConfig::isActive
                ));
        try (var writer = new FileWriter(COMMON_CONFIG.toFile())) {
            gson.toJson(configsMap, writer);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during saving common conditions config file");
            throw new RuntimeException(e);
        }
    }

    private void initConfigDirs() {
        if (!Files.exists(CONDITIONS_DIR)) {
            try {
                Files.createDirectories(CONDITIONS_DIR);
            } catch (IOException e) {
                PreventBuild.LOGGER.error("Exception during creating config directories: {}", CONDITIONS_DIR);
                throw new RuntimeException(e);
            }
        }
    }
}
