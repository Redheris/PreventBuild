package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.conditions.Condition;

public class ConditionConfig {
    private final String name;
    private transient boolean active = false;
    // TODO: Replace every category with OrCondition
    private final Condition attackAny;
    private final Condition attackBlock;
    private final Condition attackEntity;
    private final Condition interactAny;
    private final Condition interactBlock;
    private final Condition interactEntity;
    private final Condition placeBlock;
    private final Condition useItem;

    public ConditionConfig(
            String name,
            Condition attackAny,
            Condition attackBlock,
            Condition attackEntity,
            Condition interactAny,
            Condition interactBlock,
            Condition interactEntity,
            Condition placeBlock,
            Condition useItem
    ) {
        this.name = name;
        this.attackAny = attackAny;
        this.attackBlock = attackBlock;
        this.attackEntity = attackEntity;
        this.interactAny = interactAny;
        this.interactBlock = interactBlock;
        this.interactEntity = interactEntity;
        this.placeBlock = placeBlock;
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

    public Condition getAttackAny() {
        return attackAny;
    }

    public Condition getAttackBlock() {
        return attackBlock;
    }

    public Condition getAttackEntity() {
        return attackEntity;
    }

    public Condition getInteractAny() {
        return interactAny;
    }

    public Condition getInteractBlock() {
        return interactBlock;
    }

    public Condition getInteractEntity() {
        return interactEntity;
    }

    public Condition getPlaceBlock() {
        return placeBlock;
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
        private Condition attackAny;
        private Condition attackBlock;
        private Condition attackEntity;
        private Condition interactAny;
        private Condition interactBlock;
        private Condition interactEntity;
        private Condition placeBlock;
        private Condition useItem;

        public void setName(String name) {
            this.name = name;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setAttackAny(Condition attackAny) {
            this.attackAny = attackAny;
        }

        public void setAttackBlock(Condition attackBlock) {
            this.attackBlock = attackBlock;
        }

        public void setAttackEntity(Condition attackEntity) {
            this.attackEntity = attackEntity;
        }

        public void setInteractAny(Condition interactAny) {
            this.interactAny = interactAny;
        }

        public void setInteractBlock(Condition interactBlock) {
            this.interactBlock = interactBlock;
        }

        public void setInteractEntity(Condition interactEntity) {
            this.interactEntity = interactEntity;
        }

        public void setPlaceBlock(Condition placeBlock) {
            this.placeBlock = placeBlock;
        }

        public void setUseItem(Condition useItem) {
            this.useItem = useItem;
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
                    placeBlock,
                    useItem
            ).setActive(active);
        }
    }
}
