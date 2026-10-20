package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.conditions.Condition;

public class ConditionConfig {
    private final String name;
    private transient boolean active = false;
    // TODO: Replace every category with OrCondition
    private final Condition attackBlock;
    private final Condition placeBlock;
    private final Condition interactBlock;
    private final Condition attackEntity;
    private final Condition interactEntity;
    private final Condition useItem;

    public ConditionConfig(String name, Condition attackBlock, Condition placeBlock, Condition interactBlock, Condition attackEntity, Condition interactEntity, Condition useItem) {
        this.name = name;
        this.attackBlock = attackBlock;
        this.placeBlock = placeBlock;
        this.interactBlock = interactBlock;
        this.attackEntity = attackEntity;
        this.interactEntity = interactEntity;
        this.useItem = useItem;
    }

    ConditionConfig setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Condition getAttackBlock() {
        return attackBlock;
    }

    public Condition getPlaceBlock() {
        return placeBlock;
    }

    public Condition getInteractBlock() {
        return interactBlock;
    }

    public Condition getAttackEntity() {
        return attackEntity;
    }

    public Condition getInteractEntity() {
        return interactEntity;
    }

    public Condition getUseItem() {
        return useItem;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private boolean active = false;
        private Condition attackBlock;
        private Condition placeBlock;
        private Condition interactBlock;
        private Condition attackEntity;
        private Condition interactEntity;
        private Condition useItem;

        public void setName(String name) {
            this.name = name;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setAttackBlock(Condition attackBlock) {
            this.attackBlock = attackBlock;
        }

        public void setPlaceBlock(Condition placeBlock) {
            this.placeBlock = placeBlock;
        }

        public void setInteractBlock(Condition interactBlock) {
            this.interactBlock = interactBlock;
        }

        public void setAttackEntity(Condition attackEntity) {
            this.attackEntity = attackEntity;
        }

        public void setInteractEntity(Condition interactEntity) {
            this.interactEntity = interactEntity;
        }

        public void setUseItem(Condition useItem) {
            this.useItem = useItem;
        }

        public ConditionConfig build() {
            return new ConditionConfig(
                    name,
                    attackBlock,
                    placeBlock,
                    interactBlock,
                    attackEntity,
                    interactEntity,
                    useItem
            ).setActive(active);
        }
    }
}
