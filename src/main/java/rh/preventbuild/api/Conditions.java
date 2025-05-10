package rh.preventbuild.api;

import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.advanced.*;
import rh.preventbuild.conditions.basic.OrCondition;
import rh.preventbuild.conditions.blocks.*;
import rh.preventbuild.conditions.coordinates.*;
import rh.preventbuild.conditions.entities.EntityEqualsCondition;
import rh.preventbuild.conditions.items.HeldItemCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Conditions {
    public static void register() {

        // Coordinates

        ConditionRegistry.register("x:", XEqualsCondition::parse);
        ConditionRegistry.register("y:", YEqualsCondition::parse);
        ConditionRegistry.register("z:", ZEqualsCondition::parse);
        ConditionRegistry.register("x=:", (category, value) ->
                new XEqualsCondition(category, new int[]{Integer.parseInt(value)})
        );
        ConditionRegistry.register("y=:", (category, value) ->
                new YEqualsCondition(category, new int[]{Integer.parseInt(value)})
        );
        ConditionRegistry.register("z=:", (category, value) ->
                new ZEqualsCondition(category, new int[]{Integer.parseInt(value)})
        );
        ConditionRegistry.register("x>:", (category, value) ->
                new XAboveCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("y>:", (category, value) ->
                new YAboveCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("z>:", (category, value) ->
                new ZAboveCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("x<:", (category, value) ->
                new XBelowCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("y<:", (category, value) ->
                new YBelowCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("z<:", (category, value) ->
                new ZBelowCondition(category, Integer.parseInt(value))
        );
        ConditionRegistry.register("x>=", (category, value) ->
                new OrCondition(
                        new XEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new XAboveCondition(category, Integer.parseInt(value))
                )
        );
        ConditionRegistry.register("y>=", (category, value) ->
                new OrCondition(
                        new YEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new YAboveCondition(category, Integer.parseInt(value))
                )
        );
        ConditionRegistry.register("z>=", (category, value) ->
                new OrCondition(
                        new ZEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new ZAboveCondition(category, Integer.parseInt(value))
                )
        );
        ConditionRegistry.register("x<=", (category, value) ->
                new OrCondition(
                        new XEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new XBelowCondition(category, Integer.parseInt(value))
                )
        );
        ConditionRegistry.register("y<=", (category, value) ->
                new OrCondition(
                        new YEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new YBelowCondition(category, Integer.parseInt(value))
                )
        );
        ConditionRegistry.register("z<=", (category, value) ->
                new OrCondition(
                        new ZEqualsCondition(category, new int[]{Integer.parseInt(value)}),
                        new ZBelowCondition(category, Integer.parseInt(value))
                )
        );

        // Blocks

        ConditionRegistry.register("block:", ((category, value) ->
                new BlockEqualsCondition(category, parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("blockAbove:", ((category, value) ->
                new BlockAboveCondition(category, parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("blockBelow:", ((category, value) ->
                new BlockBelowCondition(category, parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("blockAdj:", ((category, value) ->
                new BlockAdjacentCondition(category, parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("lookingAt:", ((category, value) ->
                new LookingAtBlockCondition(parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("replaceBlock:", ((category, value) ->
                new ReplaceBlockCondition(parseIdsList(value, "block.minecraft"))
        ));
        ConditionRegistry.register("replaceBlock", ((category, value) ->
                new ReplaceBlockCondition(null)
        ));

        // Items

        ConditionRegistry.register("item:", (category, value) ->
            new HeldItemCondition(parseIdsList(value,"item.minecraft"))
        );

        // Advanced

        ConditionRegistry.register("stripWood", (category, value) ->
                new AxeStrippingCondition()
        );
        ConditionRegistry.register("stripWoodExcept:", AxeStrippingCondition::parse);
        ConditionRegistry.register("carpetOnCarpet", (category, value) ->
                new CarpetOnCarpetCondition(category)
        );
        ConditionRegistry.register("doubleSlab", (category, value) ->
                new DoubleSlabCondition(category)
        );
        ConditionRegistry.register("clickThrough", (category, value) ->
                new ClickThroughCondition(category, 1)
        );
        ConditionRegistry.register("clickThroughWhen:", (category, value) -> {
                int sneaking_mode = switch (value.trim()) {
                    case "standing" -> 1;
                    case "sneaking" -> 2;
                    default -> 0;
                };
                return new ClickThroughCondition(category, sneaking_mode);
        });
        ConditionRegistry.register("isSneaking:", (category, value) ->
                new IsSneakingCondition(Boolean.parseBoolean(value))
        );
        ConditionRegistry.register("dimension:", (category, value) ->
                new DimensionCondition(value)
        );
        ConditionRegistry.register("hand:", (category, value) ->
                new HandTypeCondition(value)
        );

        // Entities

        ConditionRegistry.register("entity:", (category, value) ->
            new EntityEqualsCondition(category, parseIdsList(value, "entity.minecraft"))
        );
    }

    /**
     * Converts a {@code String} of Minecraft translation keys separated by comma to {@code String[]}, also appending
     * values from ore dictionaries to it if required by using a hash character at the beginning of the value
     * @param value The {@code String} of translation keys separated by comma
     * @param prefix The {@code String} that is added to the beginning of the value if the value is a simple name
     * @return {@code String[]} of values
     */
    public static String[] parseIdsList(String value, String prefix) {
        String[] values = value.split(",");
        List<String> newValues = new ArrayList<>();
        for (String val : values) {
            if (val.startsWith("#")) {
                String dictKey = val.substring(1);
                newValues.addAll(Arrays.asList(Objects.requireNonNull(PreventBuildConfig.getOreDictionary(dictKey))));
            }
            else if (!val.contains("."))
                newValues.add(prefix + "." + val);
            else
                newValues.add(val);
        }
        return newValues.toArray(new String[0]);
    }
}
