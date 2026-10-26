package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionConfigsReader {
    public static List<ConditionConfig> readConditions(Map<Path, String> failedConfigFiles) throws IOException {
        try (var stream = Files.newDirectoryStream(PreventBuildConfig.CONDITIONS_DIR, "*.yml")) {
            List<ConditionConfig> configs = new ArrayList<>();

            for (Path path : stream) {
                ConditionConfig config = tryReadAndParse(path, failedConfigFiles);
                if (config == null) continue;
                configs.add(config);
            }

            return configs;
        }
    }

    private static @Nullable ConditionConfig tryReadAndParse(Path filepath, Map<Path, String> failedConfigFiles) {
        try {
            return ConditionConfigParser.readFromFile(filepath);
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading the condition file: {}", filepath, e);
            failedConfigFiles.put(filepath, e.getMessage());
        } catch (FailedToParseException e) {
            PreventBuild.LOGGER.error("Failed to parse the condition config file \"{}\": {}", filepath, e.getMessage(), e);
            failedConfigFiles.put(filepath, e.getMessage());
        }
        return null;
    }
}
