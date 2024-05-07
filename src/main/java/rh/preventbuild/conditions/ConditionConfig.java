package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rh.preventbuild.conditions.basic.NullCondition;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class ConditionConfig {
    private static final Path conditionsDirPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild/conditions");
    private static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");
    private final String name;
    private ICondtition condition;

    public ConditionConfig(String filename) {
        ICondtition condition = getConditionFromConfig(filename);
        String name = getName(filename);
        this.name = name;
        this.condition = condition;
    }
    public ConditionConfig(String name, ICondtition condition) {
        this.name = name;
        this.condition = condition;
    }
    public ConditionConfig(String name, String filename) throws IOException {
        createConfig(name, filename);
        this.name = name;
        this.condition = new NullCondition();
    }

    private ConditionConfig createConfig(String name, String filename) throws IOException {
        String path = conditionsDirPath.toString();
        FileWriter fileWriter = new FileWriter(path + "/" + filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);

        printWriter.print(jsonObject);

        printWriter.close();
        fileWriter.close();
        return new ConditionConfig(name, new NullCondition());
    }

    public static ICondtition getConditionFromConfig(String filename) {
        try {
            File file = conditionsDirPath.resolve( filename + ".json").toFile();
            Object o = new JSONParser().parse(new FileReader(file));
            JSONObject config = (JSONObject) o;
            LOGGER.info("Reading condition file: " + conditionsDirPath);
            ICondtition cond =  read(config);
            LOGGER.info("Successfully read config file: " + filename + ".json");
            return cond;
        } catch (IOException e) {
            LOGGER.error("Failed to read config file: " + filename + ". Check if this file exists");
            throw new RuntimeException(e);
        } catch (ParseException e) {
            LOGGER.error("Failed to parse config file: " + filename + ". Check if it has valid JSON");
            throw new RuntimeException(e);
        }
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
     * A function that reads a JSON object and generates a condition based on the content.
     *
     * @param  config	the JSON object containing the configuration
     * @return         	the condition generated from the JSON object
     */
    private static ICondtition read(JSONObject config) {
        for (Object elemName : config.keySet()) {
            if (elemName.equals("name")) continue;
            if (Arrays.asList("and", "or", "not").contains(elemName)) {
                JSONObject elemJSON = (JSONObject) config.get(elemName);
                int ind = 0;
                ICondtition[] conditions = new ICondtition[elemJSON.size()];
                for (Object condName : elemJSON.keySet()) {
                    Object cond = elemJSON.get(condName);
                    if (cond instanceof JSONObject) {
                        JSONObject condJSON = new JSONObject();
                        condJSON.put(condName, cond);
                        conditions[ind++] = read(condJSON);
                    }
                    else {
                        int[] nums;
                        if (cond instanceof JSONArray) {
                            nums = new int[((JSONArray) cond).size()];
                            for (int i = 0; i < nums.length; i++)
                                nums[i] = ((Long) ((JSONArray) cond).get(i)).intValue();
                        } else
                            nums = new int[]{((Long) cond).intValue()};
                        conditions[ind++] = ConditionsNotation.createCondition((String) condName, nums);
                    }
                }
                return ConditionsNotation.createCondition((String) elemName, conditions);
            } else {
                Object elem = config.get(elemName);
                int[] nums;
                if (elem instanceof JSONArray) {
                    nums = new int[((JSONArray) elem).size()];
                    for (int i = 0; i < nums.length; i++)
                        nums[i] = ((Long) ((JSONArray) elem).get(i)).intValue();
                } else
                    nums = new int[]{((Long) elem).intValue()};
                return ConditionsNotation.createCondition((String) elemName, nums);
            }
        }
        return new NullCondition();
    }

    public void update() {
        this.condition = getConditionFromConfig(this.name);
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

    public ICondtition getConditionFromConfig() {
        return condition;
    }
    /** Check of breaking block */
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return condition.check(player, x, y, z);
    }
    /** Check of placing block */
    public boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        return condition.check(player, x, y, z, hitResult);
    }
}
