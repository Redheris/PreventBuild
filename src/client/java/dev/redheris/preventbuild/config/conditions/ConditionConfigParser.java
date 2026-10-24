package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.conditions.MockCondition;
import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
                String key = entry.getKey();

                if (key.equals("name")) {
                    builder.setName(requireString(entry.getValue()));
                } else {
                    OrCondition condition = new OrCondition(List.of(
                            new MockCondition(entry.getValue())
                    ));
                    switch (key) {
                        // TODO: Replace mock conditions
                        case "attackAny" -> builder.setAttackAny(condition);
                        case "attackBlock" -> builder.setAttackBlock(condition);
                        case "attackEntity" -> builder.setAttackEntity(condition);
                        case "interactAny" -> builder.setInteractAny(condition);
                        case "interactBlock" -> builder.setInteractBlock(condition);
                        case "interactEntity" -> builder.setInteractEntity(condition);
                        case "useItem" -> builder.setUseItem(condition);
                        case "placeBlock" -> builder.setPlaceBlock(condition);
                    }
                }
            }

            return builder.build();

        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading the condition file: {}", filepath);
            throw new RuntimeException(e);
        }
    }
}
