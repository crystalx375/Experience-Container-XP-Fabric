package crystal.register;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static crystal.ExperienceContainer.MOD_ID;
import static crystal.register.RegisterBlockItem.*;

public class NewItemGroup {
    private static final ItemGroup BARREL_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(CHERRY_BARREL))
            .displayName(Text.translatable("itemGroup.barrel_group"))
            .entries((context, entries) -> {
                entries.add(ACACIA_BARREL);
                entries.add(BIRCH_BARREL);
                entries.add(CHERRY_BARREL);
                entries.add(CRIMSON_BARREL);
                entries.add(DARK_BARREL);
                entries.add(JUNGLE_BARREL);
                entries.add(OAK_BARREL);
                entries.add(SPRUCE_BARREL);
                entries.add(WARPED_BARREL);            })
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "barrel_group"), BARREL_GROUP);
    }
}
