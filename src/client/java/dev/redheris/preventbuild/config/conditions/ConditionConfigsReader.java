package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.config.PreventBuildConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConditionConfigsReader {
    public static List<ConditionConfig> readConditions() {
        try (var stream = Files.newDirectoryStream(PreventBuildConfig.CONDITIONS_DIR, "*.yml")) {
            List<ConditionConfig> configs = new ArrayList<>();

            for (Path path : stream) {
                ConditionConfig config = ConditionConfigParser.readFromFile(path);
                configs.add(config);
            }

            return configs;
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading conditions directory", e);
            throw new RuntimeException("Exception during reading conditions directory", e);
        }
    }
}
