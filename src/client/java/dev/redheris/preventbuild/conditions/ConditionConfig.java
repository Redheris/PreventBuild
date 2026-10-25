package dev.redheris.preventbuild.conditions;

import dev.redheris.preventbuild.conditions.impl.logical.OrCondition;

public record ConditionConfig(String name, OrCondition attackAny, OrCondition attackBlock, OrCondition attackEntity,
                              OrCondition interactAny, OrCondition interactBlock, OrCondition interactEntity,
                              OrCondition useItem, OrCondition placeBlock) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
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
            );
        }
    }
}
