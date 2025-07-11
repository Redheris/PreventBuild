package rh.preventbuild;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rh.preventbuild.conditions.ConditionConfig;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static rh.preventbuild.client.PreventBuildClient.LOGGER;

@SuppressWarnings("unchecked")
public class PreventBuildConfig {
    private static final Path PBConfigsPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild");
    public static JSONObject condConfigsHandler = new JSONObject();
    private static final Map<String, Path> conditionConfigPaths = new java.util.HashMap<>();
    private static final Map<String, ConditionConfig> conditionConfigs = new java.util.HashMap<>();
    public static final Map<String, String[]> oreDictionary = new java.util.HashMap<>();

    private static Text exceptionMessage;
    private static Path readingConfigFilePath;

    public static void loadConfigs() {
        exceptionMessage = null;
        String current = "";
        try {
            current = "preventbuild.oredict_load_error";
            PreventBuildConfig.loadOreDictionary();
            current = "preventbuild.configs_load_error";
            PreventBuildConfig.loadConditionConfigs();
        } catch (Exception e) {
            MutableText prefix = Text.literal("[PreventBuild] ").formatted(Formatting.DARK_AQUA);
            Text configFilename = Text.literal(readingConfigFilePath.getFileName().toString()).styled(
                    style -> style
                            .withClickEvent(new ClickEvent.OpenFile(readingConfigFilePath))
                            .withHoverEvent(new HoverEvent.ShowText(Text.translatable("preventbuild.open_config_file")))
                            .withUnderline(true)
            );
            exceptionMessage = prefix.append(Text.translatable(current, configFilename)
                    .append(": " + e.getCause().getMessage()).formatted(Formatting.RED));
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.sendMessage(exceptionMessage, false);
            }
        }
    }

    public static Text getExceptionMessage() {
        return exceptionMessage;
    }

    public static void loadConditionConfigs() {
        try {
            conditionConfigs.clear();
            File jsonFile = PBConfigsPath.resolve("condition_configs.json").toFile();
            if (jsonFile.createNewFile()) {
                PrintWriter writer = new PrintWriter(jsonFile);
                writer.println("{}");
                writer.close();
            }
            Object o = new JSONParser().parse(new FileReader(jsonFile));
            condConfigsHandler = (JSONObject) o;

            Path conditionDir = PBConfigsPath.resolve("conditions");
            if (!Files.exists(conditionDir)) {
                Files.createDirectories(conditionDir);
                Files.createFile(conditionDir.resolve("put_your_configs_here"));
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(conditionDir, "*.cfg")) {
                boolean found = false;
                for (Path path : stream) {
                    readingConfigFilePath = path;
                    found = true;

                    String filename = path.getFileName().toString();

                    ConditionConfig config = new ConditionConfig(filename);
                    conditionConfigs.put(config.getName(), config);
                    conditionConfigPaths.put(config.getName(), path);

                    if (!condConfigsHandler.containsKey(config.getName())) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("active", true);
                        condConfigsHandler.put(config.getName(), jsonObject);
                    }

                }

                if (!found) {
                    LOGGER.info("No conditions configs found");
                    return;
                }

            } catch (IOException e) {
                LOGGER.error("Failed to read condition configs directory", e);
                return;
            }

            Object[] keys = condConfigsHandler.keySet().toArray();
            for (Object key : keys) {
                if (!conditionConfigs.containsKey((String) key)) {
                    condConfigsHandler.remove(key);
                }
            }

            if (!conditionConfigs.isEmpty()) {
                LOGGER.info("Found {} conditions configs:", condConfigsHandler.size());
                for (String configName : conditionConfigs.keySet()) {
                    boolean isActive = (Boolean)((JSONObject)(condConfigsHandler.get(configName))).get("active");
                    LOGGER.info("Config \"{}\" : {}", configName, isActive ? "active" : "disabled");
                }
            }

            FileWriter fileWriter = new FileWriter(PBConfigsPath + "/condition_configs.json");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(prettyPrintJSON(condConfigsHandler.toString()));
            printWriter.close();
            fileWriter.close();
        } catch (ParseException e) {
            LOGGER.error("Failed to parse JSON config file. Check the format of the file\n{}", e.getMessage(), e);
        }
        catch (Exception e) {
            LOGGER.error("Failed to load conditions configs: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void loadOreDictionary() {
        try {
            if (!Files.exists(PBConfigsPath))
                Files.createDirectories(PBConfigsPath);
            oreDictionary.clear();
            File jsonFile = PBConfigsPath.resolve("ore_dictionaries.json").toFile();
            if (jsonFile.createNewFile()) {
                PrintWriter writer = new PrintWriter(jsonFile);
                writer.println("{}");
                writer.close();
                return;
            }
            Object o = new JSONParser().parse(new FileReader(jsonFile));
            JSONObject oreDictJSON = (JSONObject) o;

            for (Object key : oreDictJSON.keySet().toArray()) {
                JSONArray valuesList = (JSONArray) oreDictJSON.get(key);
                String[] ores = new String[valuesList.size()];
                for (int i = 0; i < valuesList.size(); i++) {
                    ores[i] = (String) valuesList.get(i);
                }
                oreDictionary.put(key.toString(), ores);
            }
        }
        catch (ParseException e) {
            LOGGER.error("Failed to parse JSON config file. Check the format of the file:\n{}", e.getMessage(), e);
        }
        catch (Exception e) {
            LOGGER.error("Failed to load ore dictionary file:\n{}", e.getMessage(), e);
        }
    }

    public static boolean hasConfig(String configName) {
        return conditionConfigs.containsKey(configName);
    }

    public static String[] getOreDictionary(String key) {
        if (oreDictionary.containsKey(key))
            return oreDictionary.get(key);
        LOGGER.error("Ore dictionary \"{}\" not found", key);
        String message = Text.translatable("preventbuild.oredict_not_found", key).getString();
        throw new RuntimeException(message);
    }

    public static ConditionConfig getConditionConfig(String name) {
        return conditionConfigs.get(name);
    }

    public static Path getConditionConfigPath(String name) {
        return conditionConfigPaths.get(name);
    }

    public static Map<String, Boolean> getConfigsList() {
        Map<String, Boolean> list = new java.util.HashMap<>();
        for (String configName : conditionConfigs.keySet()) {
            boolean isActive = (Boolean)((JSONObject)(condConfigsHandler.get(configName))).get("active");
            list.put(configName, isActive);
        }
        return list;
    }

    public static boolean isConfigEnabled(String configName) {
        if (condConfigsHandler.containsKey(configName)) {
            return (Boolean)((JSONObject)condConfigsHandler.get(configName)).get("active");
        }
        return false;
    }

    public static int switchConfigEnabled(String configName) {
        try {
            if (!condConfigsHandler.containsKey(configName)) {
                return -1;
            }
            JSONObject jsonObject = (JSONObject) condConfigsHandler.get(configName);
            jsonObject.put("active", !((Boolean) jsonObject.get("active")));
            condConfigsHandler.put(configName, jsonObject);

            FileWriter fileWriter = new FileWriter(PBConfigsPath + "/condition_configs.json");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(prettyPrintJSON(condConfigsHandler.toString()));
            printWriter.close();
            fileWriter.close();
        }
        catch (Exception e) {
            LOGGER.error("Failed to write file\n{}", e.getMessage(), e);
        }

        return 0;
    }


    /**
     * A simple implementation to pretty-print JSON file.
     */
    public static String prettyPrintJSON(String unformattedJsonString) {
        StringBuilder prettyJSONBuilder = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;
        for(char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
            switch(charFromUnformattedJson) {
                case '"':
                    // switch the quoting status
                    inQuote = !inQuote;
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    break;
                case ' ':
                    // For space: ignore the space if it is not being quoted.
                    if(inQuote) {
                        prettyJSONBuilder.append(charFromUnformattedJson);
                    }
                    break;
                case '{':
                case '[':
                    // Starting a new block: increase the indent level
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    indentLevel++;
                    appendIndentedNewLine(indentLevel, prettyJSONBuilder);
                    break;
                case '}':
                case ']':
                    // Ending a new block; decrese the indent level
                    indentLevel--;
                    appendIndentedNewLine(indentLevel, prettyJSONBuilder);
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    break;
                case ',':
                    // Ending a json item; create a new line after
                    prettyJSONBuilder.append(charFromUnformattedJson);
                    if(!inQuote) {
                        appendIndentedNewLine(indentLevel, prettyJSONBuilder);
                    }
                    break;
                default:
                    prettyJSONBuilder.append(charFromUnformattedJson);
            }
        }
        return prettyJSONBuilder.toString();
    }

    /**
     * Print a new line with indention at the beginning of the new line.
     */
    private static void appendIndentedNewLine(int indentLevel, StringBuilder stringBuilder) {
        stringBuilder.append("\n");
        // Assuming indention using 2 spaces
        stringBuilder.append("  ".repeat(Math.max(0, indentLevel)));
    }
}
