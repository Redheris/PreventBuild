package rh.preventbuild;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.literal;

public class PreventBuild implements ModInitializer {

    public static PreventBuildConfig config = new PreventBuildConfig();

//    private void genLab(ServerPlayerEntity player) {
//        int xPos = (int) player.getPos().getX();
//        int yPos = (int) player.getPos().getY();
//        int zPos = (int) player.getPos().getZ();
//        System.out.println("Hi!" + xPos + " " + yPos + " " + zPos);
//
//
//        boolean[][] lab = new boolean[17][17];
//        int uniqCounter = 1;
//
//        // Step1.  First line
//        int[] setNumber = new int[16];
//        Arrays.fill(setNumber, 0);
//
//        // Шаги. находящиеся в цикле (2+)
//        for (int i = 0; i < 17; i+=2) {
//            // Step 2. Unique sets
//            for (int j = 0; j < setNumber.length; ++j) {
//                if (setNumber[j] == 0)
//                    setNumber[j] = uniqCounter++;
//            }
//
//            Random rand = new Random();
//            // Step 3. Right walls
//            for (int j = 0; j < lab[i].length - 1; ++j) {
//                if (rand.nextInt(3) == 0) {
//                    lab[i][j + 1] = true;
//                    j++;
//                } else setNumber[j + 1] = setNumber[j];
//            }
//            // Step 4. Bottom walls
//            boolean wallLine = false;
//            for (int j = 0; j < lab[i].length - 1; ++j) {
//
//                boolean check = false;
//                for (int n = 0; n < setNumber.length; ++n) {
//                    if (n != j && setNumber[n] == setNumber[j] && !lab[i + 1][j]) {
//                        check = true;
//                        break;
//                    }
//                }
//                if (!check) continue;
//
//                if (rand.nextInt(3) == 0) {
//                    lab[i + 1][j] = true;
//                    wallLine = true;
//                }
//            }
////            if (wallLine && i < lab.length - 2) i++;
//
//            // Step 5
//            // 5A. New line
//            if (i < lab.length - 3) {
//                lab[i + 2] = lab[i];
//                Arrays.fill(lab[i], false);
//                for (int j = 0; j < lab[i].length; ++j) {
//                    if (lab[i+1][j])
//                        setNumber[j] = 0;
//                }
//            }
//            // 5B. Complete the lab
//            else {
//                for (int j = 0; j < lab[i].length - 1; ++j){
//                    if (setNumber[j] != setNumber[j + 2] && lab[i][j + 1]) {
//                        lab[i][j + 1] = false;
//                        setNumber[j + 1] = setNumber[j];
//                        setNumber[j + 2] = setNumber[j];
//                    }
//                }
//                for (int j = 0; j < lab[i].length; ++j)
//                    lab[i + 1][j] = true;
//            }
//        }
//
//
//        ServerWorld world = (ServerWorld) player.getWorld();
//
//        int xOld = xPos;
//
//        for (boolean[] i : lab) {
//            for (boolean j : i) {
////                System.out.print((j ? block : air) + " ");
//                if (j)
//                    world.setBlockState(new BlockPos(xPos, yPos, zPos), Blocks.OAK_PLANKS.getDefaultState());
//                xPos++;
//            }
//            xPos = xOld;
//            zPos++;
//        }
//    }

//    static void emptyArr(boolean[] a) {
//        for (boolean i : a)
//            i = false;
//    }


    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rh")
                .executes(context -> {
                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                    context.getSource().sendMessage(Text.literal("Called /rh with no arguments"));
                    if (context.getSource().getPlayer() != null) {

                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player.getActiveItem() != null)
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                                    Text.translatable(player.getMainHandStack().getItem().getTranslationKey() + "\n"
                                            + Blocks.STONE.getTranslationKey())
                            );

//                        genLab(player);
                    }

                    return 1;
                })));


        AutoConfig.register(PreventBuildConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(PreventBuildConfig.class).getConfig();

        AutoConfig.getConfigHolder(PreventBuildConfig.class).registerLoadListener((configHolder, preventBuildConfig) -> {

            regIntList(BlockingLists.getBreakY(), config.breakY, "breakY");
            regIntList(BlockingLists.getPlaceY(), config.placeY, "placeY");
            regBlockList(BlockingLists.getBreakBlocks(), config.breakBlocks);

            return ActionResult.CONSUME;
        });

        AutoConfig.getConfigHolder(PreventBuildConfig.class).registerSaveListener((configHolder, preventBuildConfig) -> {

            config = AutoConfig.getConfigHolder(PreventBuildConfig.class).getConfig();

            regIntList(BlockingLists.getBreakY(), config.breakY, "breakY");
            regIntList(BlockingLists.getPlaceY(), config.placeY, "placeY");
            regBlockList(BlockingLists.getBreakBlocks(), config.breakBlocks);

            return ActionResult.CONSUME;
        });

        AutoConfig.getConfigHolder(PreventBuildConfig.class).load();
    }

    private void regIntList(ArrayList<Integer> blockList, String configList, String listName) {

        String s = configList.replaceAll(", ", ",");

        if (s.length() == 0) {
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

        if(s.length() == 0) {
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
            double d = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
