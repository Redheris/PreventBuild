package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.advanced.*;
import rh.preventbuild.conditions.basic.*;
import rh.preventbuild.conditions.blocks.*;
import rh.preventbuild.conditions.coordinates.*;
import rh.preventbuild.conditions.entities.ClickThroughCondition;
import rh.preventbuild.conditions.entities.EntityCondition;
import rh.preventbuild.conditions.items.HeldItemCondition;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static rh.preventbuild.conditions.ConditionCategory.*;

public class ConditionConfig {
    private static final Path conditionsDirPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild/conditions");
    private static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");
    private final String name;
    private final ICondition breakCondition;
    private final ICondition placeCondition;
    private final ICondition otherCondition;
    private final ICondition interactCondition;
    private final ICondition attackCondition;

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
                           ICondition interactCondition, ICondition attackCondition) {
        this.name = name;
        this.breakCondition = breakCondition;
        this.placeCondition = placeCondition;
        this.otherCondition = otherCondition;
        this.interactCondition = interactCondition;
        this.attackCondition = attackCondition;
    }

    public static ConditionConfig getConditionFromConfig(String filename) {
        LOGGER.info("Reading condition file: " + conditionsDirPath + "\\" + filename + ".cfg");
        ConditionConfig config = read(conditionsDirPath.resolve( filename + ".cfg"));
        assert config != null;
        LOGGER.info("Successfully read config \"" + config.name + "\" from file: " + filename + ".cfg");
        return config;
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

            ICondition breakCondition = new NullCondition();
            ICondition placeCondition = new NullCondition();
            ICondition otherCondition = new NullCondition();
            ICondition interactCondition = new NullCondition();
            ICondition attackCondition = new NullCondition();

            for (int i = 0; i < configLines.length; i++) {
                int tabLevel = getTabLevel(configLines[i]);
                String line = configLines[i].trim();
                if (tabLevel == 0) {
                    if (line.startsWith("name:"))
                        configurationName = line.substring(5).trim();
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
                            interactCondition = readLogicalCondition(INTERACT_ENTITY, configPart);
                            break;
                        case "attackEntity:":
                            attackCondition = readLogicalCondition(ATTACK_ENTITY, configPart);
                            break;
                    }
                    i += configPart.length - 2;
                }
            }

            return new ConditionConfig(
                    configurationName, breakCondition, placeCondition, otherCondition, interactCondition, attackCondition
            );

        } catch (FileNotFoundException exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

    private static ICondition readCondition(ConditionCategory category, String line) {
        line = line.trim();
        String key = line.substring(0, line.indexOf(":") + 1);
        String value = line.substring(line.indexOf(":") + 1).trim();
        if (key.isEmpty())
            key = line;

        switch (key) { // TODO: Rewrite to some "conditions registry" format
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
            case "block:":
            case "blockAbove:":
            case "blockBelow:":
            case "blockAdj:":
            case "lookingAt:": {
                String[] values = value.split(",");
                List<String> newValues = new ArrayList<>();
                for (String val : values) {
                    if (val.startsWith("#")) {
                        String dictKey = val.substring(1);
                        newValues.addAll(Arrays.asList(PreventBuildConfig.getOreDictionary(dictKey)));
                    }
                    else if (!val.contains("."))
                        newValues.add("block.minecraft." + val);
                    else
                        newValues.add(val);
                }
                values = newValues.toArray(new String[0]);
                switch (key) {
                    case "block:": return new BlockEqualCondition(category, values);
                    case "blockAbove:": return new BlockAboveCondition(category, values);
                    case "blockBelow:": return new BlockBelowCondition(category, values);
                    case "blockAdj:": return new BlockAdjacentCondition(category, values);
                    case "lookingAt:": return new LookingAtBlockCondition(values);
                }
            }
            case "item:": {
                String[] values = value.split(",");
                List<String> newValues = new ArrayList<>();
                for (String val : values) {
                    if (val.startsWith("#")) {
                        String dictKey = val.substring(1);
                        newValues.addAll(Arrays.asList(PreventBuildConfig.getOreDictionary(dictKey)));
                    }
                    else if (!val.contains("."))
                        newValues.add("item.minecraft." + val);
                    else
                        newValues.add(val);
                }
                values = newValues.toArray(new String[0]);
                return new HeldItemCondition(values);
            }
            case "stripWood": return new AxeStrippingCondition();
            case "stripWoodExcept:": {
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++)
                    if (!values[i].contains("."))
                        values[i] = "block.minecraft." + values[i];
                return new AxeStrippingCondition(values);
            }
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
            case "isSneaking:": return new IsSneakingCondition(Boolean.parseBoolean(value));
            case "dimension:": return new DimensionCondition(value);
            case "hand:": return new HandTypeCondition(value);
            case "entity:": {
                String[] values = value.split(",");
                List<String> newValues = new ArrayList<>();
                for (String val : values) {
                    if (val.startsWith("#")) {
                        String dictKey = val.substring(1);
                        newValues.addAll(Arrays.asList(PreventBuildConfig.getOreDictionary(dictKey)));
                    }
                    else if (!val.contains("."))
                        newValues.add("entity.minecraft." + val);
                    else
                        newValues.add(val);
                }
                values = newValues.toArray(new String[0]);
                return new EntityEqualsCondition(category,values);
            }
        }

        return new NullCondition();
    }

    private static ICondition readLogicalCondition(ConditionCategory category, String[] lines) {
        switch (lines[0].trim()){
            case "not:": {
                String nextLine = lines[1].trim();
                int indWithoutComms = 1;
                while ((nextLine.startsWith("%") || nextLine.startsWith("#")) && indWithoutComms < lines.length - 1) {
                    nextLine = lines[++indWithoutComms].trim();
                }
                if (nextLine.startsWith("and:") || nextLine.startsWith("not:") || nextLine.startsWith("or:"))
                    return new NotCondition(readLogicalCondition(category, Arrays.copyOfRange(lines, 1, lines.length)));
                return new NotCondition(readCondition(category, nextLine));
            }
            case "and:":
            case "break:":
            case "place:":
            case "other:":
            case "interactEntity:":
            case "attackEntity:":
            case "or:": {
                ArrayList<ICondition> conditions = new ArrayList<>();
                String[] condLines = Arrays.copyOfRange(lines, 1, lines.length);
                for (int i = 0; i < condLines.length; i++) {
                    String param = condLines[i].trim();
                    while ((param.startsWith("%") || param.startsWith("#")) && i < condLines.length - 1)
                        param = condLines[++i].trim();
                    if (param.startsWith("and:") || param.startsWith("not:") || param.startsWith("or:")) {
                        String[] newCondString = cutTabLevel(Arrays.copyOfRange(condLines, i, condLines.length));
                        conditions.add(readLogicalCondition(category, newCondString));
                        i += newCondString.length - 1;
                    } else {
                        ICondition newCond = readCondition(category, param);
                        if (!(newCond instanceof NullCondition))
                            conditions.add(newCond);
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
            if (line.trim().startsWith("#") || line.trim().startsWith("%")) {
                res.add(line);
                continue;
            }
            if (getTabLevel(line) <= tabLevel && i != 0)
                break;
            res.add(line);
        }
        String[] result = new String[res.size()];
        for (int i = 0; i < res.size(); i++)
            result[i] = res.get(i);

        return result;
    }
}
