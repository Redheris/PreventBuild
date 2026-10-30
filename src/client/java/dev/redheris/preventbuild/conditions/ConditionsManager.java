package dev.redheris.preventbuild.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import dev.redheris.preventbuild.config.conditions.ActiveConfigsStorage;
import dev.redheris.preventbuild.config.conditions.ConditionConfigsReader;
import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ConditionsManager {
    private final List<ConditionConfig> configs = new ArrayList<>();
    private final Set<ConditionConfig> activeConfigs = new HashSet<>();

    public List<ConditionConfig> getConfigs() {
        return configs;
    }

    public Set<ConditionConfig> getActiveConfigs() {
        return activeConfigs;
    }

    public @Nullable ConditionConfig findConfigByName(String configName) {
        for (ConditionConfig config : configs) {
            if (config.name().equals(configName)) {
                return config;
            }
        }
        return null;
    }

    public boolean isActive(ConditionConfig config) {
        return activeConfigs.contains(config);
    }

    public void setActive(ConditionConfig config, boolean value) {
        if (isActive(config) == value) return;

        if (value) {
            activeConfigs.add(config);
        } else {
            activeConfigs.remove(config);
        }
        save();
    }

    /// @return a {@link Map} containing pairs "config file path":"fail reason"
    public Map<Path, String> read(RegistryAccess registries) {
        PreventBuildConfig.ensureConfigDirectoriesExists();
        configs.clear();
        activeConfigs.clear();

        Set<String> activeConfigNames;
        Map<Path, String> failedConfigFiles = new HashMap<>();
        List<ConditionConfig> updated;

        try {
            activeConfigNames = ActiveConfigsStorage.read();
        } catch (Exception e) {
            PreventBuild.LOGGER.error("Exception during reading active conditions config file", e);
            failedConfigFiles.put(ActiveConfigsStorage.STORAGE_FILE_PATH, e.getMessage());
            activeConfigNames = new HashSet<>();
        }

        try {
            updated = ConditionConfigsReader.readConditions(registries, failedConfigFiles);
            configs.addAll(updated);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading conditions directory", e);
            return failedConfigFiles;
        }

        for (ConditionConfig config : configs) {
            if (activeConfigNames.contains(config.name())) {
                activeConfigs.add(config);
            }
        }

        PreventBuild.LOGGER.info("Found and read {} condition config file", updated.size());
        PreventBuild.LOGGER.info("Currently active condition configs count: {}", activeConfigs.size());

        return failedConfigFiles;
    }

    public void save() {
        try {
            Set<String> activeConfigNames = activeConfigs.stream()
                    .map(ConditionConfig::name)
                    .collect(Collectors.toSet());
            ActiveConfigsStorage.save(activeConfigNames);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during saving common conditions config file");
        }
    }
}
