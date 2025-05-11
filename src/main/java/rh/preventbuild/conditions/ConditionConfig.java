package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rh.preventbuild.api.ConditionRegistry;
import rh.preventbuild.conditions.basic.AndCondition;
import rh.preventbuild.conditions.basic.NotCondition;
import rh.preventbuild.conditions.basic.NullCondition;
import rh.preventbuild.conditions.basic.OrCondition;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
                    if (param.startsWith("%") || param.startsWith("#"))
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
