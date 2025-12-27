package rh.preventbuild.event.categories;

public class CategoriesRegister {
    public static void registerAll() {
        InteractBlockCategory.register();
        UseItemCategory.register();

        BreakBlockCategory.register();
        PlaceBlockCategory.register();

        InteractEntityCategory.register();
        AttackEntityCategory.register();
    }
}
