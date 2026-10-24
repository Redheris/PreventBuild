package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;

public class ConditionConfig {
    private final String name;
    private transient boolean active = false;
    private final OrCondition attackAny;
    private final OrCondition attackBlock;
    private final OrCondition attackEntity;
    private final OrCondition interactAny;
    private final OrCondition interactBlock;
    private final OrCondition interactEntity;
    private final OrCondition useItem;
    private final OrCondition placeBlock;

    public ConditionConfig(
            String name,
            OrCondition attackAny,
            OrCondition attackBlock,
            OrCondition attackEntity,
            OrCondition interactAny,
            OrCondition interactBlock,
            OrCondition interactEntity,
            OrCondition useItem,
            OrCondition placeBlock
    ) {
        this.name = name;
        this.attackAny = attackAny;
        this.attackBlock = attackBlock;
        this.attackEntity = attackEntity;
        this.interactAny = interactAny;
        this.interactBlock = interactBlock;
        this.interactEntity = interactEntity;
        this.useItem = useItem;
        this.placeBlock = placeBlock;
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

    public OrCondition getAttackAny() {
        return attackAny;
    }

    public OrCondition getAttackBlock() {
        return attackBlock;
    }

    public OrCondition getAttackEntity() {
        return attackEntity;
    }

    public OrCondition getInteractAny() {
        return interactAny;
    }

    public OrCondition getInteractBlock() {
        return interactBlock;
    }

    public OrCondition getInteractEntity() {
        return interactEntity;
    }

    public OrCondition getUseItem() {
        return useItem;
    }

    public OrCondition getPlaceBlock() {
        return placeBlock;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private boolean active = false;
        private OrCondition attackAny;
        private OrCondition attackBlock;
        private OrCondition attackEntity;
        private OrCondition interactAny;
        private OrCondition interactBlock;
        private OrCondition interactEntity;
        private OrCondition useItem;
        private OrCondition placeBlock;

        public void setName(String name) {
            this.name = name;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setAttackAny(OrCondition attackAny) {
            this.attackAny = attackAny;
        }

        public void setAttackBlock(OrCondition attackBlock) {
            this.attackBlock = attackBlock;
        }

        public void setAttackEntity(OrCondition attackEntity) {
            this.attackEntity = attackEntity;
        }

        public void setInteractAny(OrCondition interactAny) {
            this.interactAny = interactAny;
        }

        public void setInteractBlock(OrCondition interactBlock) {
            this.interactBlock = interactBlock;
        }

        public void setInteractEntity(OrCondition interactEntity) {
            this.interactEntity = interactEntity;
        }

        public void setUseItem(OrCondition useItem) {
            this.useItem = useItem;
        }

        public void setPlaceBlock(OrCondition placeBlock) {
            this.placeBlock = placeBlock;
        }

        public ConditionConfig build() {
            return new ConditionConfig(
                    name,
                    attackAny,
                    attackBlock,
                    attackEntity,
                    interactAny,
                    interactBlock,
                    interactEntity,
                    useItem,
                    placeBlock
            ).setActive(active);
        }
    }
}
