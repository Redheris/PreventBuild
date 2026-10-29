package dev.redheris.preventbuild.conditions.impl;

import dev.redheris.preventbuild.api.ConditionFactory;
import dev.redheris.preventbuild.api.ConditionRegistry;
import dev.redheris.preventbuild.api.ConditionValueFormatException;
import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockBesideCondition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockReplaceCondition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockStateCondition;
import dev.redheris.preventbuild.conditions.impl.coordinates.*;
import dev.redheris.preventbuild.conditions.impl.entities.EntityEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.items.ItemCustomNameCondition;
import dev.redheris.preventbuild.conditions.impl.items.ItemEnchantmentCondition;
import dev.redheris.preventbuild.conditions.impl.items.ItemEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.logical.AndCondition;
import dev.redheris.preventbuild.conditions.impl.logical.NotCondition;
import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;
import dev.redheris.preventbuild.conditions.impl.player.GameModeCondition;
import dev.redheris.preventbuild.conditions.impl.player.InteractionHandCondition;
import dev.redheris.preventbuild.conditions.impl.player.IsCrouchingCondition;
import dev.redheris.preventbuild.conditions.impl.world.BiomeCondition;
import dev.redheris.preventbuild.conditions.impl.world.DimensionCondition;
import dev.redheris.preventbuild.conditions.impl.world.IsSingleplayerCondition;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameType;

import java.util.Map;

import static dev.redheris.preventbuild.conditions.util.ConditionValueExtractor.*;
import static dev.redheris.preventbuild.conditions.util.ConditionValueValidator.*;

public class Conditions {
    private static boolean alreadyRegistered = false;
    private final static ConditionRegistry registry = ConditionRegistry.getInstance();

    public static void registerAll() {
        if (alreadyRegistered) return;

        // Logical
        register("and", (regs, value) ->
                new AndCondition(extractConditionList(regs, value))
        );
        register("or", (regs, value) ->
                new OrCondition(extractConditionList(regs, value))
        );
        register("not", (regs, value) -> {
            var entry = extractEntry(value);
            return new NotCondition(registry.parseCondition(regs, entry.getKey(), entry.getValue()));
        });

        // Blocks
        register("block", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockEqualsCondition(identifiers);
        });
        register("blockAbove", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.UP);
        });
        register("blockBelow", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.DOWN);
        });
        register("blockNorth", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.NORTH);
        });
        register("blockSouth", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.SOUTH);
        });
        register("blockWest", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.WEST);
        });
        register("blockEast", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.EAST);
        });
        register("blockState", (regs, value) -> {
            var entry = extractEntry(value);
            return new BlockStateCondition(entry.getKey(), extractStringList(entry.getValue()));
        });
        register("replaceBlock", (regs, value) ->
                new BlockReplaceCondition(requireBoolean(value))
        );

        // Items
        register("item", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("ITEM", BuiltInRegistries.ITEM, value);
            return new ItemEqualsCondition(identifiers);
        });
        register("customName", (regs, value) -> parseCustomNameCondition(value));
        register("enchant", Conditions::parseItemEnchantmentCondition);

        // Entities
        register("entity", (regs, value) -> {
            var identifiers =
                    extractIdentifierTagList("ENTITY_TYPE", BuiltInRegistries.ENTITY_TYPE, value);
            return new EntityEqualsCondition(identifiers);
        });

        // Player
        register("isCrouching", (regs, value) ->
                new IsCrouchingCondition(requireBoolean(value))
        );
        register("isShiftKeyDown", (regs, value) ->
                new IsCrouchingCondition(requireBoolean(value))
        );
        register("hand", (regs, value) ->
                new InteractionHandCondition(InteractionHand.valueOf(requireString(value).toUpperCase()))
        );
        register("gameMode", (regs, value) ->
                new GameModeCondition(GameType.byName(requireString(value)))
        );

        // World
        register("dimension", (regs, value) -> {
            var registry = regs.lookupOrThrow(Registries.DIMENSION_TYPE);
            return new DimensionCondition(extractIdentifierTagList("DIMENSION_TYPE", registry, value));
        });
        register("biome", (regs, value) -> {
            var registry = regs.lookupOrThrow(Registries.BIOME);
            return new BiomeCondition(extractIdentifierTagList("BIOME", registry, value));
        });
        register("isSingleplayer", (regs, value) ->
                new IsSingleplayerCondition(requireBoolean(value))
        );

        // Coordinates
        register("x", (regs, value) -> parseCoordinateEqualsOrWithin(Direction.Axis.X, value));
        register("y", (regs, value) -> parseCoordinateEqualsOrWithin(Direction.Axis.Y, value));
        register("z", (regs, value) -> parseCoordinateEqualsOrWithin(Direction.Axis.Z, value));
        register("x<", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS
                ));
        register("y<", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS
                ));
        register("z<", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS
                ));
        register("x<=", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS_EQ
                ));
        register("y<=", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS_EQ
                ));
        register("z<=", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS_EQ
                ));
        register("x>", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER
                ));
        register("y>", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER
                ));
        register("z>", (regs, value) ->
                new CoordinateCompareCondition(Direction.Axis.Z, requireInt(value), IntComparator.GREATER)
        );
        register("x>=", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("y>=", (regs, value) ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("z>=", (regs, value) ->
                new CoordinateCompareCondition(Direction.Axis.Z, requireInt(value), IntComparator.GREATER_EQ)
        );
        register("player_x", (regs, value) -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.X, value));
        register("player_y", (regs, value) -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.Y, value));
        register("player_z", (regs, value) -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.Z, value));
        register("player_x<", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS)
        );
        register("player_y<", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS)
        );
        register("player_z<", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS)
        );
        register("player_x<=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS_EQ)
        );
        register("player_y<=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS_EQ)
        );
        register("player_z<=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS_EQ)
        );
        register("player_x>", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER)
        );
        register("player_y>", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER)
        );
        register("player_z>", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER)
        );
        register("player_x>=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER_EQ)
        );
        register("player_y>=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER_EQ)
        );
        register("player_z>=", (regs, value) ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER_EQ)
        );

        alreadyRegistered = true;
    }

    private static Condition parseItemEnchantmentCondition(RegistryAccess regs, Object value) {
        if (value instanceof Map<?, ?> map) {
            Integer level = null;
            if (map.containsKey("level")) {
                level = requireInt(map.get("level"));
            }
            IdentifierTagWrapper<Enchantment> enchantment = extractIdentifierTag(
                    "ENCHANTMENT",
                    regs.lookupOrThrow(Registries.ENCHANTMENT),
                    requireString(map.get("id"))
            );
            return new ItemEnchantmentCondition(enchantment, level);
        }
        IdentifierTagWrapper<Enchantment> enchantment = extractIdentifierTag(
                "ENCHANTMENT",
                regs.lookupOrThrow(Registries.ENCHANTMENT),
                requireString(value)
        );
        return new ItemEnchantmentCondition(enchantment, null);
    }

    private static Condition parseCustomNameCondition(Object value) {
        if (value instanceof Map<?, ?> map) {
            boolean regex = false;
            if (map.containsKey("regex")) {
                regex = requireBoolean(map.get("regex"));
            }
            String name = requireString(map.get("name"));
            return new ItemCustomNameCondition(regex, name);
        }

        String name = requireString(value);
        return new ItemCustomNameCondition(false, name);
    }

    private static Condition parseCoordinateEqualsOrWithin(Direction.Axis axis, Object value) {
        if (value instanceof String stringValue) {
            try {
                String[] rangeStr = stringValue.split("\\.\\.|~|:");
                int min = Integer.parseInt(rangeStr[0]);
                int max = Integer.parseInt(rangeStr[1]);
                return new CoordinateWithinCondition(axis, min, max);
            } catch (NumberFormatException e) {
                throw new ConditionValueFormatException(e);
            }
        }
        return new CoordinateEqualsCondition(axis, extractIntArray(value));
    }

    private static Condition parsePlayerCoordinateEqualsOrWithin(Direction.Axis axis, Object value) {
        if (value instanceof String stringValue) {
            try {
                String[] rangeStr = stringValue.split("\\.\\.|~|:");
                int min = Integer.parseInt(rangeStr[0]);
                int max = Integer.parseInt(rangeStr[1]);
                return new PlayerCoordinateWithinCondition(axis, min, max);
            } catch (NumberFormatException e) {
                throw new ConditionValueFormatException(e);
            }
        }
        return new PlayerCoordinateEqualsCondition(axis, extractIntArray(value));
    }

    private static void register(String key, ConditionFactory factory) {
        registry.register(key, factory);
    }
}
