package rh.preventbuild;

import net.minecraft.block.Block;

import java.util.ArrayList;

public class BlockingLists {
    private static final ArrayList<Integer> blockedBreakY = new ArrayList<>();
    private static final ArrayList<Integer> blockedPlaceY = new ArrayList<>();
    private static final ArrayList<String> blockedBreakBlocks = new ArrayList<>();

    public static ArrayList<Integer> getBreakY() {
        return blockedBreakY;
    }

    public static ArrayList<Integer> getPlaceY() {
        return blockedPlaceY;
    }

    public static ArrayList<String> getBreakBlocks() {
        return blockedBreakBlocks;
    }

}
