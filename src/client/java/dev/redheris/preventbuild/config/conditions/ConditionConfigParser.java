package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.api.ConditionRegistry;
import dev.redheris.preventbuild.api.UnexpectedValueTypeException;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static dev.redheris.preventbuild.util.ConditionValueValidator.requireMap;
import static dev.redheris.preventbuild.util.ConditionValueValidator.requireString;

public class ConditionConfigParser {
    public static @Nullable ConditionConfig readFromFile(Path filepath) {
        try (var reader = new FileReader(filepath.toFile())) {
            var builder = ConditionConfig.builder();

            String filename = filepath.getFileName().toString();
            builder.setName(FilenameUtils.getBaseName(filename));

            var settings = LoadSettings.builder()
                    .setLabel("Condition config file (" + filepath.getFileName() + ")")
                    .build();
            Load load = new Load(settings);

            @SuppressWarnings("unchecked")
            Map<String, ?> root = (Map<String, ?>) requireMap(load.loadFromReader(reader));

            for (Map.Entry<String, ?> entry : root.entrySet()) {
                String key = entry.getKey();

                if (key.equals("name")) {
                    builder.setName(requireString(entry.getValue()));
                } else {
                    OrCondition condition = (OrCondition) ConditionRegistry.getInstance()
                            .parseCondition("or", entry.getValue());
                    switch (key) {
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
        } catch (UnexpectedValueTypeException e) {
            PreventBuild.LOGGER.error("Invalid structure in the condition file: {}", filepath);
            return null;
        } catch (IOException e) {
            PreventBuild.LOGGER.error("Exception during reading the condition file: {}", filepath);
            return null;
        }
    }
}
