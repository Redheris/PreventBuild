package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.conditions.MockCondition;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static dev.redheris.preventbuild.config.conditions.ConditionConfigValidator.requireMap;
import static dev.redheris.preventbuild.config.conditions.ConditionConfigValidator.requireString;

public class ConditionConfigParser {
    public static ConditionConfig readFromFile(Path filepath) {
        try (var reader = new FileReader(filepath.toFile())) {
            var builder = ConditionConfig.builder();

            var settings = LoadSettings.builder()
                    .setLabel("Condition config file (" + filepath.getFileName() + ")")
                    .build();
            Load load = new Load(settings);

            Map<String, ?> root = (Map<String, ?>) requireMap(load.loadFromReader(reader));

            for (Map.Entry<String, ?> entry : root.entrySet()) {
                switch (entry.getKey()) {
                    case "name" -> builder.setName(requireString(entry.getValue()));
                    // TODO: Replace mock conditions
                    case "break" -> builder.setAttackBlock(new MockCondition(entry.getValue()));
                    case "place" -> builder.setPlaceBlock(new MockCondition(entry.getValue()));
                    case "interactBlock" -> builder.setInteractBlock(new MockCondition(entry.getValue()));
                    case "useItem" -> builder.setUseItem(new MockCondition(entry.getValue()));
                    case "interactEntity" -> builder.setInteractEntity(new MockCondition(entry.getValue()));
                    case "attackEntity" -> builder.setAttackEntity(new MockCondition(entry.getValue()));
                }
            }

            return builder.build();

        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading the condition file: {}", filepath);
            throw new RuntimeException(e);
        }
    }
}
