package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rh.preventbuild.api.ConditionRegistry;
import rh.preventbuild.conditions.basic.AndCondition;
import rh.preventbuild.conditions.basic.NotCondition;
import rh.preventbuild.conditions.basic.NullCondition;
import rh.preventbuild.conditions.basic.OrCondition;
import rh.preventbuild.conditions.categories.ConditionCategory;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static rh.preventbuild.conditions.categories.ConditionCategory.*;

public class ConditionConfig {
    private static final Path conditionsDirPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild/conditions");
    private static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");
    private final String name;
    private final ICondition useItemCondition;
    private final ICondition interactBlockCondition;
    private final ICondition breakCondition;
    private final ICondition placeCondition;
    private final ICondition interactEntityCondition;
    private final ICondition attackEntityCondition;

    public ConditionConfig(String filename) {
        ConditionConfig config = getConditionFromConfig(filename);
        this.name = config.name;
        this.useItemCondition = config.useItemCondition;
        this.interactBlockCondition = config.interactBlockCondition;
        this.breakCondition = config.breakCondition;
        this.placeCondition = config.placeCondition;
        this.interactEntityCondition = config.interactEntityCondition;
        this.attackEntityCondition = config.attackEntityCondition;
    }
    public ConditionConfig(String name, ICondition useItemCondition, ICondition interactBlockCondition, ICondition breakCondition,
                           ICondition placeCondition, ICondition interactEntityCondition, ICondition attackEntityCondition) {
        this.name = name;
        this.useItemCondition = useItemCondition;
        this.interactBlockCondition = interactBlockCondition;
        this.breakCondition = breakCondition;
        this.placeCondition = placeCondition;
        this.interactEntityCondition = interactEntityCondition;
        this.attackEntityCondition = attackEntityCondition;
    }

    public static ConditionConfig getConditionFromConfig(String filename) {
        LOGGER.info("Reading condition file: {}.cfg", conditionsDirPath + "\\" + filename);
        ConditionConfig config = read(conditionsDirPath.resolve( filename + ".cfg"));
        assert config != null;
        LOGGER.info("Successfully read config \"{}\" from file: {}.cfg", config.name, filename);
        return config;
    }

    /**
     * Reads the config file and generates a condition config based on its content
     *
     * @param  configPath	path to the file containing the configuration
     * @return         	    the {@code ConditionConfig} generated from the config
     */
    private static ConditionConfig read(Path configPath) {
        try {
            String[] configLines = new Scanner(configPath.toFile()).useDelimiter("\\Z").next().split("\n");
            String configurationName = "Unnamed Configuration";

            ICondition useItemCondition = new NullCondition();
            ICondition interactBlockCondition = new NullCondition();
            ICondition breakCondition = new NullCondition();
            ICondition placeCondition = new NullCondition();
            ICondition interactEntityCondition = new NullCondition();
            ICondition attackEntityCondition = new NullCondition();

            for (int i = 0; i < configLines.length; i++) {
                int tabLevel = getTabLevel(configLines[i]);
                String line = configLines[i].trim();
                if (tabLevel == 0) {
                    if (line.startsWith("name:")) {
                        if (line.contains("_"))
                            throw new IllegalArgumentException("Configuration names must not contain '_' characters");
                        configurationName = line.substring(5).trim();
                    }
                }
                else {
                    String[] configPart = cutTabLevel(Arrays.copyOfRange(configLines, i - 1, configLines.length));
                    switch (configPart[0].trim()) {
                        case "useItem:":
                            useItemCondition = readLogicalCondition(USE_ITEM, configPart);
                            break;
                        case "interactBlock:":
                            interactBlockCondition = readLogicalCondition(INTERACT_BLOCK, configPart);
                            break;
                        case "break:":
                            breakCondition = readLogicalCondition(ConditionCategory.BREAK, configPart);
                            break;
                        case "place:":
                            placeCondition = readLogicalCondition(ConditionCategory.PLACE, configPart);
                            break;
                        case "interactEntity:":
                            interactEntityCondition = readLogicalCondition(INTERACT_ENTITY, configPart);
                            break;
                        case "attackEntity:":
                            attackEntityCondition = readLogicalCondition(ATTACK_ENTITY, configPart);
                            break;
                    }
                    i += configPart.length - 2;
                }
            }

            return new ConditionConfig(
                    configurationName, useItemCondition, interactBlockCondition, breakCondition, placeCondition, interactEntityCondition, attackEntityCondition
            );

        } catch (FileNotFoundException exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

    private static ICondition readCondition(ConditionCategory category, String line) {
        line = line.trim();
        String key, value = "";

        int divIndex = line.indexOf(":");
        if (divIndex == -1)
            key = line;
        else {
            key = line.substring(0, divIndex + 1);
            value = line.substring(divIndex + 1).trim();
            if (value.isEmpty())
                throw new IllegalArgumentException("Condition with empty required value: \"" + line + "\"");
        }
        return ConditionRegistry.parse(category, key, value);
    }

    private static ICondition readLogicalCondition(ConditionCategory category, String[] lines) {
        switch (lines[0].trim()){
            case "not:": {
                String nextLine = lines[1].trim();
                int indWithoutComms = 1;
                while ((nextLine.isBlank() || nextLine.startsWith("%") || nextLine.startsWith("#")) && indWithoutComms < lines.length - 1) {
                    nextLine = lines[++indWithoutComms].trim();
                }
                if (nextLine.startsWith("and:") || nextLine.startsWith("not:") || nextLine.startsWith("or:"))
                    return new NotCondition(readLogicalCondition(category, Arrays.copyOfRange(lines, 1, lines.length)));
                return new NotCondition(readCondition(category, nextLine));
            }
            case "and:":
            case "useItem:":
            case "interactBlock:":
            case "break:":
            case "place:":
            case "interactEntity:":
            case "attackEntity:":
            case "or:": {
                ArrayList<ICondition> conditions = new ArrayList<>();
                String[] condLines = Arrays.copyOfRange(lines, 1, lines.length);
                for (int i = 0; i < condLines.length; i++) {
                    String param = condLines[i].trim();
                    if (param.isBlank() || param.startsWith("%") || param.startsWith("#"))
                        continue;
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

                if (conditionsRes.length == 0)
                    throw new IllegalArgumentException("Logical condition with zero arguments");
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
            case USE_ITEM -> useItemCondition;
            case INTERACT_BLOCK -> interactBlockCondition;
            case BREAK -> breakCondition;
            case PLACE -> placeCondition;
            case INTERACT_ENTITY -> interactEntityCondition;
            case ATTACK_ENTITY -> attackEntityCondition;
            default -> new NullCondition();
        };
    }

    public ICondition getCondition() {
        return new OrCondition(useItemCondition, interactBlockCondition, breakCondition, placeCondition, interactEntityCondition, attackEntityCondition);
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
            String lineTrim = line.trim();
            if (lineTrim.isEmpty() || lineTrim.startsWith("#") || lineTrim.startsWith("%")) {
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
