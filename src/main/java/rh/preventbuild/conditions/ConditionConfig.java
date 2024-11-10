package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rh.preventbuild.conditions.advanced.AxeStrippingCondition;
import rh.preventbuild.conditions.advanced.CarpetOnCarpetCondition;
import rh.preventbuild.conditions.advanced.DoubleSlabCondition;
import rh.preventbuild.conditions.basic.AndCondition;
import rh.preventbuild.conditions.basic.NotCondition;
import rh.preventbuild.conditions.basic.NullCondition;
import rh.preventbuild.conditions.basic.OrCondition;
import rh.preventbuild.conditions.blocks.BlockAboveCondition;
import rh.preventbuild.conditions.blocks.BlockAdjacentCondition;
import rh.preventbuild.conditions.blocks.BlockBelowCondition;
import rh.preventbuild.conditions.blocks.BlockEqualCondition;
import rh.preventbuild.conditions.coordinates.*;
import rh.preventbuild.conditions.entities.ClickThroughCondition;
import rh.preventbuild.conditions.entities.IEntityCondition;
import rh.preventbuild.conditions.entities.NullEntityCondition;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static rh.preventbuild.conditions.ConditionCategory.*;

public class ConditionConfig {
    private static final Path conditionsDirPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild/conditions");
    private static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");
    private final String name;
    private ICondition breakCondition = new NullCondition();
    private ICondition placeCondition = new NullCondition();
    private ICondition otherCondition = new NullCondition();
    private IEntityCondition interactCondition;
    private IEntityCondition attackCondition;
    private boolean enabled = true;

    public ConditionConfig(String filename) {
        ConditionConfig config = getConditionFromConfig(filename);
        this.name = config.name;
        this.breakCondition = config.breakCondition;
        this.placeCondition = config.placeCondition;
        this.otherCondition = config.otherCondition;
        this.interactCondition = config.interactCondition;
        this.attackCondition = config.attackCondition;
    }
    public ConditionConfig(String name, ICondition breakCondition, ICondition placeCondition, ICondition otherCondition,
                           IEntityCondition interactCondition, IEntityCondition attackCondition) {
        this.name = name;
        this.breakCondition = breakCondition;
        this.placeCondition = placeCondition;
        this.otherCondition = otherCondition;
        this.interactCondition = interactCondition;
        this.attackCondition = attackCondition;
    }
    public ConditionConfig(String name, String filename) throws IOException {
        createConfigFile(name, filename);
        this.name = name;
    }

    private ConditionConfig createConfigFile(String name, String filename) throws IOException {
        String path = conditionsDirPath.toString();
        FileWriter fileWriter = new FileWriter(path + "/" + filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);

        printWriter.print(jsonObject);

        printWriter.close();
        fileWriter.close();
        return new ConditionConfig(name, filename);
    }

    public static ConditionConfig getConditionFromConfig(String filename) {
        LOGGER.info("Reading condition file: " + conditionsDirPath);
        ConditionConfig config = read(conditionsDirPath.resolve( filename + ".cfg"));
        System.out.println("test string");
        assert config != null;
        LOGGER.info("Successfully read config \"" + config.name + "\" from file: " + filename + ".cfg");
        return config;
    }
    public static String getName(String filename) {
        try {
            File file = conditionsDirPath.resolve( filename + ".json").toFile();
            Object o = new JSONParser().parse(new FileReader(file));
            JSONObject config = (JSONObject) o;
            return (String) config.get("name");
        } catch (IOException e) {
            LOGGER.error("Failed to read config file: " + filename + ". Check if this file exists");
            throw new RuntimeException(e);
        } catch (ParseException e) {
            LOGGER.error("Failed to parse config file: " + filename + ". Check if it has valid JSON");
            throw new RuntimeException(e);
        }
    }
    /**
     * A function that reads a config file and generates a condition config based on the content.
     *
     * @param  configPath	path to the file containing the configuration
     * @return         	    the ConditionConfig generated from the config
     */
    private static ConditionConfig read(Path configPath) {
        try {
            String[] configLines = new Scanner(configPath.toFile()).useDelimiter("\\Z").next().split("\n");
            String configurationName = "Unnamed Configuration";
            boolean isEnabled = true;

            ICondition breakCondition = new NullCondition();
            ICondition placeCondition = new NullCondition();
            ICondition otherCondition = new NullCondition();
            IEntityCondition interactCondition = new NullEntityCondition();
            IEntityCondition attackCondition = new NullEntityCondition();

            for (int i = 0; i < configLines.length; i++) {
                int tabLevel = getTabLevel(configLines[i]);
                String line = configLines[i].trim();
                if (tabLevel == 0) {
                    if (line.startsWith("name:"))
                        configurationName = line.substring(5).trim();
                    else if (line.startsWith("enabled:"))
                        isEnabled = Boolean.parseBoolean(line.substring(8).trim());
                }
                else {
                    String[] configPart = cutTabLevel(Arrays.copyOfRange(configLines, i - 1, configLines.length));
                    switch (configPart[0].trim()) {
                        case "break:":
                            breakCondition = readLogicalCondition(ConditionCategory.BREAK, configPart);
                            break;
                        case "place:":
                            placeCondition = readLogicalCondition(ConditionCategory.PLACE, configPart);
                            break;
                        case "other:":
                            otherCondition = readLogicalCondition(OTHER, configPart);
                            break;
                        case "interactEntity:":
                            interactCondition = (IEntityCondition) readLogicalCondition(INTERACT_ENTITY, configPart);
                            break;
                        case "attackEntity:":
                            attackCondition = (IEntityCondition) readLogicalCondition(ATTACK_ENTITY, configPart);
                            break;
                    }
                    i += configPart.length - 2;
                }
            }

            return new ConditionConfig(
                    configurationName, breakCondition, placeCondition, otherCondition, interactCondition, attackCondition
            ).setEnabled(isEnabled);

        } catch (FileNotFoundException exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

    private static ICondition readCondition(ConditionCategory category, String line) {
        line = line.trim();
        String key = line.substring(0, line.indexOf(":") + 1);
        String value = line.substring(line.indexOf(":") + 1);
        if (key.isEmpty())
            key = line;
        switch (key) {
            case "x:": {
                String[] values = value.split(",");
                ICondition[] conditions = new ICondition[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (values[i].contains("~")) {
                        int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                        int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                        conditions[i] = new XWithinCondition(category, start, end);
                    } else {
                        conditions[i] = new XEqualCondition(category, new int[]{Integer.parseInt(values[i].trim())});
                    }
                }
                if (conditions.length == 1)
                    return conditions[0];
                return new OrCondition(conditions);
            }
            case "y:": {
                String[] values = value.split(",");
                ICondition[] conditions = new ICondition[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (values[i].contains("~")) {
                        int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                        int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                        conditions[i] = new YWithinCondition(category, start, end);
                    } else {
                        conditions[i] = new YEqualCondition(category, new int[]{Integer.parseInt(values[i].trim())});
                    }
                }
                if (conditions.length == 1)
                    return conditions[0];
                return new OrCondition(conditions);
            }
            case "z:": {
                String[] values = value.split(",");
                ICondition[] conditions = new ICondition[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (values[i].contains("~")) {
                        int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                        int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                        conditions[i] = new ZWithinCondition(category, start, end);
                    } else {
                        conditions[i] = new ZEqualCondition(category, new int[]{Integer.parseInt(values[i].trim())});
                    }
                }
                if (conditions.length == 1)
                    return conditions[0];
                return new OrCondition(conditions);
            }
            case "x=:": return new XEqualCondition(category, new int[]{Integer.parseInt(value)});
            case "y=:": return new YEqualCondition(category, new int[]{Integer.parseInt(value)});
            case "z=:": return new ZEqualCondition(category, new int[]{Integer.parseInt(value)});
            case "x>:": return new XAboveCondition(category, Integer.parseInt(value));
            case "y>:": return new YAboveCondition(category, Integer.parseInt(value));
            case "z>:": return new ZAboveCondition(category, Integer.parseInt(value));
            case "x<:": return new XBelowCondition(category, Integer.parseInt(value));
            case "y<:": return new YBelowCondition(category, Integer.parseInt(value));
            case "z<:": return new ZBelowCondition(category, Integer.parseInt(value));
            case "x>=:":
                return new OrCondition(
                        new XEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new XAboveCondition(category, Integer.parseInt(value))
                );
            case "y>=:":
                return new OrCondition(
                        new YEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new YAboveCondition(category, Integer.parseInt(value))
                );
            case "z>=:":
                return new OrCondition(
                        new ZEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new ZAboveCondition(category, Integer.parseInt(value))
                );
            case "x<=:":
                return new OrCondition(
                        new XEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new XBelowCondition(category, Integer.parseInt(value))
                );
            case "y<=:":
                return new OrCondition(
                        new YEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new YBelowCondition(category, Integer.parseInt(value))
                );
            case "z<=:":
                return new OrCondition(
                        new ZEqualCondition(category, new int[]{Integer.parseInt(value)}),
                        new ZBelowCondition(category, Integer.parseInt(value))
                );
            case "block:": {
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++)
                    if (!values[i].contains("."))
                        values[i] = "block.minecraft." + values[i];
                return new BlockEqualCondition(category, values);
            }
            case "blockAbove:": {
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++)
                    if (!values[i].contains("."))
                        values[i] = "block.minecraft." + values[i];
                return new BlockAboveCondition(category, values);
            }
            case "blockBelow:": {
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++)
                    if (!values[i].contains("."))
                        values[i] = "block.minecraft." + values[i];
                return new BlockBelowCondition(category, values);
            }
            case "blockAdj:": {
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++)
                    if (!values[i].contains("."))
                        values[i] = "block.minecraft." + values[i];
                return new BlockAdjacentCondition(category, values);
            }
            case "stripWood": return new AxeStrippingCondition();
            case "carpetOnCarpet": return new CarpetOnCarpetCondition(category);
            case "doubleSlab": return new DoubleSlabCondition(category);
            case "clickThrough": return new ClickThroughCondition(1);
            case "clickThroughWhen:": {
                int sneaking_mode = switch (value.trim()) {
                    case "standing" -> 1;
                    case "sneaking" -> 2;
                    default -> 0;
                };
                return new ClickThroughCondition(sneaking_mode);
            }
        }

        return new NullCondition();
    }

    private static ICondition readLogicalCondition(ConditionCategory category, String[] lines) {
        switch (lines[0].trim()){
            case "not:": {
                String nextLine = lines[1].trim();
                if (nextLine.startsWith("and:") || nextLine.startsWith("not:") || nextLine.startsWith("or:"))
                    return new NotCondition(readLogicalCondition(category, Arrays.copyOfRange(lines, 1, lines.length)));
                return new NotCondition(readCondition(category, nextLine));
            }
            case "and:":
            case "break:":
            case "place:":
            case "other:":
            case "interactEntity:":
            case "or:": {
                ArrayList<ICondition> conditions = new ArrayList<>();
                String[] condLines = Arrays.copyOfRange(lines, 1, lines.length);
                for (int i = 0; i < condLines.length; i++) {
                    String param = condLines[i].trim();
                    if (param.startsWith("and:") || param.startsWith("not:") || param.startsWith("or:")) {
                        String[] newCondString = cutTabLevel(Arrays.copyOfRange(condLines, i, condLines.length));
                        conditions.add(readLogicalCondition(category, newCondString));
                        i += newCondString.length - 1;
                    } else {
                        conditions.add(readCondition(category, param));
                    }
                }

                ICondition[] conditionsRes = new ICondition[conditions.size()];
                for (int i = 0; i < conditionsRes.length; i++) {
                    conditionsRes[i] = conditions.get(i);
                }

                if (conditionsRes.length == 1)
                    return conditionsRes[0];
                if (lines[0].trim().startsWith("and:"))
                    return new AndCondition(conditionsRes);
                return new OrCondition(conditionsRes);
            }
            default: return new NullCondition();
        }
    }

    private static ICondition readLogicalCondition(ConditionCategory category, String configPart) {
        return readLogicalCondition(category, configPart.split("\n"));
    }

    private ConditionConfig setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void update() {
        ConditionConfig newConfig = getConditionFromConfig(this.name);
//        this.condition = newConfig.getCondition();
    }

    public void save() {
        try {
            String path = conditionsDirPath.toString();
            FileWriter fileWriter = new FileWriter(path + "/" + this.name);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);

            printWriter.print(jsonObject);

            printWriter.close();
            fileWriter.close();
        }
        catch (IOException e) {
            LOGGER.error("Failed to save config file: " + this.name);
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public ICondition getCondition(ConditionCategory category) {
        return switch (category) {
            case BREAK -> breakCondition;
            case PLACE -> placeCondition;
            case OTHER -> otherCondition;
            case INTERACT_ENTITY -> interactCondition;
            case ATTACK_ENTITY -> attackCondition;
            default -> new NullCondition();
        };
    }

    public ICondition getCondition() {
        return new OrCondition(breakCondition, placeCondition, otherCondition, interactCondition, attackCondition);
    }

    private static int getTabLevel(String line) {
        int tabLevel = 0;
        for (char ch : line.toCharArray()) {
            if (ch == '\t')
                tabLevel++;
        }
        return tabLevel;
    }

    private static String[] cutTabLevel(String[] lines) {
        int tabLevel = getTabLevel(lines[0]);
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (getTabLevel(line) <= tabLevel && i != 0)
                break;
            res.add(line);
        }
        String[] result = new String[res.size()];
        for (int i = 0; i < res.size(); i++)
            result[i] = res.get(i);

        return result;
    }

    private static String[] cutTabLevel(String str) {
        return cutTabLevel(str.split("\n"));
    }
}
