package dev.redheris.preventbuild.config.customtags;

import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.redheris.preventbuild.config.PreventBuildConfig.gson;

/**
 * Custom tags system containing {@link List} of {@link Identifier} without complex registry logic
 * and binding to a specific registry
 */
public class CustomTagsManager {
    private static final Path TAGS_FILE_PATH = PreventBuildConfig.CONFIGS_DIR.resolve("custom_tags.json");
    private static final Map<String, List<Identifier>> tags = new HashMap<>();

    public static boolean isIn(String tagName, Identifier value) {
        var list = tags.get(tagName);
        return list != null && list.contains(value);
    }

    public static boolean containsTag(String tagName) {
        return tags.containsKey(tagName);
    }

    /// @return exception message, {@code null} if no exceptions were caught
    public static @Nullable String read() {
        tags.clear();

        if (Files.exists(TAGS_FILE_PATH)) {
            try (var reader = new FileReader(TAGS_FILE_PATH.toFile())) {
                var tagsMap = gson.fromJson(
                        reader, new TypeToken<Map<String, List<Identifier>>>() {}
                );
                tags.putAll(tagsMap);
                return save();
            } catch (IOException e) {
                PreventBuild.LOGGER.error("Failed to read custom tags file", e);
                return e.getMessage();
            } catch (Exception e) {
                PreventBuild.LOGGER.error("Failed to parse custom tags file", e);
                return e.getMessage();
            }
        } else {
            tags.put("example", List.of(Identifier.parse("stone"), Identifier.parse("dirt")));
            tags.put("example_2", List.of(Identifier.parse("stone"), Identifier.parse("dirt")));
            return save();
        }
    }

    /// @return exception message, {@code null} if no exceptions were caught
    public static @Nullable String save() {
        try (var writer = new FileWriter(TAGS_FILE_PATH.toFile())) {
            gson.toJson(tags, writer);
            return null;
        } catch (JsonIOException | IOException e) {
            PreventBuild.LOGGER.error("Failed to write custom tags file", e);
            return e.getMessage();
        }
    }
}
