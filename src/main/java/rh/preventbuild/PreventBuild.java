package rh.preventbuild;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.basic.OrCondition;

import java.util.ArrayList;
import java.util.Collections;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static rh.preventbuild.PreventBuildConfig.condConfigsHandler;

public class PreventBuild implements ModInitializer {

    public static PreventBuildConfig oldConfig = new PreventBuildConfig();
//    public static ConditionConfig config;

    @Override
    public void onInitialize() {



//        AutoConfig.register(PreventBuildConfig.class, JanksonConfigSerializer::new);
//        oldConfig = AutoConfig.getConfigHolder(PreventBuildConfig.class).getConfig();
//
//        AutoConfig.getConfigHolder(PreventBuildConfig.class).registerLoadListener((configHolder, preventBuildConfig) -> {
//
//            regIntList(BlockingLists.getBreakY(), oldConfig.breakY, "breakY");
//            regIntList(BlockingLists.getPlaceY(), oldConfig.placeY, "placeY");
//            regBlockList(BlockingLists.getBreakBlocks(), oldConfig.breakBlocks);
//
//            return ActionResult.CONSUME;
//        });
//
//        AutoConfig.getConfigHolder(PreventBuildConfig.class).registerSaveListener((configHolder, preventBuildConfig) -> {
//
//            oldConfig = AutoConfig.getConfigHolder(PreventBuildConfig.class).getConfig();
//
//            regIntList(BlockingLists.getBreakY(), oldConfig.breakY, "breakY");
//            regIntList(BlockingLists.getPlaceY(), oldConfig.placeY, "placeY");
//            regBlockList(BlockingLists.getBreakBlocks(), oldConfig.breakBlocks);
//
//            return ActionResult.CONSUME;
//        });
//
//        AutoConfig.getConfigHolder(PreventBuildConfig.class).load();
    }

    private void regIntList(ArrayList<Integer> blockList, String configList, String listName) {

        String s = configList.replaceAll(", ", ",");

        if (s.isEmpty()) {
            blockList.clear();
            return;
        }

        boolean exception = false;
        String[] list = s.split(",");

        blockList.clear();

        for (String i : list) {
            if (isNumber(i))
                blockList.add(Integer.parseInt(i));
            else
                exception = true;
        }

        Collections.sort(blockList);

        if (exception) {
            if (MinecraftClient.getInstance().inGameHud != null) {
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                        Text.translatable("preventbuild.exception.incorrect_list_format", listName)
                );
            }
        }
    }

    private void regBlockList(ArrayList<String> blockList, String configList) {

        String s = configList.replaceAll(", ", ",");

        if(s.isEmpty()) {
            blockList.clear();
            return;
        }

        boolean exception = false;
        String[] list = s.split(",");

        blockList.clear();

        for (String i : list)
            blockList.add("block.minecraft." + i);

//        for (String i : list) {
//            Block bl = BlockStateArgumentType.getBlockState(null, i).getBlockState().getBlock();
//            if (bl != null)
//                blockList.add(i);
//            else
//                exception = true;
//        }

        if (exception) {
            if (MinecraftClient.getInstance().inGameHud != null) {
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                        Text.translatable("preventbuild.exception.incorrect_list_format")
                );
            }
        }
    }

    private boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
