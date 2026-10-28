package dev.redheris.preventbuild.conditions.impl;

import dev.redheris.preventbuild.api.ConditionFactory;
import dev.redheris.preventbuild.api.ConditionRegistry;
import dev.redheris.preventbuild.api.ConditionValueFormatException;
import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockBesideCondition;
import dev.redheris.preventbuild.conditions.impl.blocks.BlockEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.coordinates.*;
import dev.redheris.preventbuild.conditions.impl.entities.EntityEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.items.ItemEqualsCondition;
import dev.redheris.preventbuild.conditions.impl.logical.AndCondition;
import dev.redheris.preventbuild.conditions.impl.logical.NotCondition;
import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;
import dev.redheris.preventbuild.conditions.impl.player.GameModeCondition;
import dev.redheris.preventbuild.conditions.impl.player.InteractionHandCondition;
import dev.redheris.preventbuild.conditions.impl.player.IsCrouchingCondition;
import dev.redheris.preventbuild.conditions.impl.world.BiomeCondition;
import dev.redheris.preventbuild.conditions.impl.world.DimensionCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import static dev.redheris.preventbuild.conditions.util.ConditionValueExtractor.*;
import static dev.redheris.preventbuild.conditions.util.ConditionValueValidator.*;

public class Conditions {
    private static boolean alreadyRegistered = false;
    private final static ConditionRegistry registry = ConditionRegistry.getInstance();

    public static void registerAll() {
        if (alreadyRegistered) return;

        // Logical
        register("and", value -> new AndCondition(extractConditionList(value)));
        register("or", value -> new OrCondition(extractConditionList(value)));
        register("not", value -> {
            var entry = extractEntry(value);
            return new NotCondition(registry.parseCondition(entry.getKey(), entry.getValue()));
        });

        // Blocks
        register("block", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockEqualsCondition(identifiers);
        });
        register("blockAbove", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.UP);
        });
        register("blockBelow", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.DOWN);
        });
        register("blockNorth", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.NORTH);
        });
        register("blockSouth", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.SOUTH);
        });
        register("blockWest", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.WEST);
        });
        register("blockEast", value -> {
            var identifiers =
                    extractIdentifierTagList("BLOCK", BuiltInRegistries.BLOCK, value);
            return new BlockBesideCondition(identifiers, Direction.EAST);
        });

        // Items
        register("item", value -> {
            var identifiers =
                    extractIdentifierTagList("ITEM", BuiltInRegistries.ITEM, value);
            return new ItemEqualsCondition(identifiers);
        });

        // Entities
        register("entity", value -> {
            var identifiers =
                    extractIdentifierTagList("ENTITY_TYPE", BuiltInRegistries.ENTITY_TYPE, value);
            return new EntityEqualsCondition(identifiers);
        });

        // Player
        register("isCrouching", value -> new IsCrouchingCondition(requireBoolean(value)));
        register("isShiftKeyDown", value -> new IsCrouchingCondition(requireBoolean(value)));
        register("hand", value ->
                new InteractionHandCondition(InteractionHand.valueOf(requireString(value).toUpperCase()))
        );
        register("gameMode", value ->
                new GameModeCondition(GameType.byName(requireString(value)))
        );

        // World
        register("dimension", value -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) {
                System.out.println("Naaaaah");
                throw new IllegalStateException("Cannot create the condition without a Level");
            }
            var registry = level.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
            return new DimensionCondition(extractIdentifierTagList("DIMENSION_TYPE", registry, value));
        });
        register("biome", value -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) {
                System.out.println("Naaaaah");
                throw new IllegalStateException("Cannot create the condition without a Level");
            }
            var registry = level.registryAccess().lookupOrThrow(Registries.BIOME);
            return new BiomeCondition(extractIdentifierTagList("BIOME", registry, value));
        });

        // Coordinates
        register("x", value -> parseCoordinateEqualsOrWithin(Direction.Axis.X, value));
        register("y", value -> parseCoordinateEqualsOrWithin(Direction.Axis.Y, value));
        register("z", value -> parseCoordinateEqualsOrWithin(Direction.Axis.Z, value));
        register("x<", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS
                ));
        register("y<", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS
                ));
        register("z<", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS
                ));
        register("x<=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS_EQ
                ));
        register("y<=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS_EQ
                ));
        register("z<=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS_EQ
                ));
        register("x>", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER
                ));
        register("y>", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER
                ));
        register("z>", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER
                ));
        register("x>=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("y>=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("z>=", value ->
                new CoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("player_x", value -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.X, value));
        register("player_y", value -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.Y, value));
        register("player_z", value -> parsePlayerCoordinateEqualsOrWithin(Direction.Axis.Z, value));
        register("player_x<", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS
                ));
        register("player_y<", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS
                ));
        register("player_z<", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS
                ));
        register("player_x<=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.LESS_EQ
                ));
        register("player_y<=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.LESS_EQ
                ));
        register("player_z<=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.LESS_EQ
                ));
        register("player_x>", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER
                ));
        register("player_y>", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER
                ));
        register("player_z>", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER
                ));
        register("player_x>=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.X, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("player_y>=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Y, requireInt(value), IntComparator.GREATER_EQ
                ));
        register("player_z>=", value ->
                new PlayerCoordinateCompareCondition(
                        Direction.Axis.Z, requireInt(value), IntComparator.GREATER_EQ
                ));

        alreadyRegistered = true;
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
