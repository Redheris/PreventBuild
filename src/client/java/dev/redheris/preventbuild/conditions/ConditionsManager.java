package dev.redheris.preventbuild.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import dev.redheris.preventbuild.config.conditions.ActiveConfigsStorage;
import dev.redheris.preventbuild.config.conditions.ConditionConfigsReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void read() {
        PreventBuildConfig.ensureConfigDirectoriesExists();
        configs.clear();
        activeConfigs.clear();

        List<ConditionConfig> updated = ConditionConfigsReader.readConditions();
        configs.addAll(List.copyOf(updated));

        try {
            Set<String> activeConfigNames = ActiveConfigsStorage.read();
            for (ConditionConfig config : configs) {
                if (activeConfigNames.contains(config.name())) {
                    activeConfigs.add(config);
                }
            }
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading common conditions config file");
            throw new RuntimeException(e);
        }

        save();
    }

    public void save() {
        try {
            Set<String> activeConfigNames = activeConfigs.stream()
                    .map(ConditionConfig::name)
                    .collect(Collectors.toSet());
            ActiveConfigsStorage.save(activeConfigNames);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during saving common conditions config file");
            throw new RuntimeException(e);
        }
    }
}
