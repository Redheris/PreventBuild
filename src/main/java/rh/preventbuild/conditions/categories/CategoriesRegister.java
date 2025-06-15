package rh.preventbuild.conditions.categories;

public class CategoriesRegister {
    public static void registerAll() {
        UseItemCategory.register();
        InteractBlockCategory.register();

        BreakBlockCategory.register();
        PlaceBlockCategory.register();

        InteractEntityCategory.register();
        AttackEntityCategory.register();
    }
}
