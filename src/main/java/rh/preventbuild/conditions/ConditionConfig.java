package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rh.preventbuild.conditions.basic.NullCondition;

import java.io.*;
import java.nio.file.Path;

public class ConditionConfig {
    private static final Path conditionsDirPath = FabricLoader.getInstance().getConfigDir().resolve("preventbuild/conditions");
    private static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");
    private final String name;
    private ICondition condition;

    public ConditionConfig(String filename) {
        ICondition condition = getConditionFromConfig(filename);
        String name = getName(filename);
        this.name = name;
        this.condition = condition;
    }
    public ConditionConfig(String name, ICondition condition) {
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

    public static ICondition getConditionFromConfig(String filename) {
        try {
            File file = conditionsDirPath.resolve( filename + ".json").toFile();
            Object o = new JSONParser().parse(new FileReader(file));
            JSONObject config = (JSONObject) o;
            LOGGER.info("Reading condition file: " + conditionsDirPath);
            ICondition cond = read(conditionsDirPath.resolve( filename + ".json"));
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
    private static ICondition read(Path configPath) {
        try {
            FileInputStream fstream = new FileInputStream(configPath.toFile());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            int tabLevel = 0;


            while ((strLine = br.readLine()) != null) {

            }

            in.close();
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getMessage());
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

    public ICondition getCondition() {
        return condition;
    }
    /** Check of breaking block */
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return condition.check(player, hand, x, y, z);
    }
    /** Check of placing block */
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return condition.check(player, hand, x, y, z, hitResult);
    }
}
