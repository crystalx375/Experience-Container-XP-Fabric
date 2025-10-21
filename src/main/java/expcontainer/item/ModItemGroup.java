package expcontainer.item;

import expcontainer.Experience_Container;
import expcontainer.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// Tab Items
public class ModItemGroup {
    public static final ItemGroup ExpContainer_Group = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Experience_Container.MOD_ID,
                    "expcontainer"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.expcontainer"))
                    .icon(() -> new ItemStack(ModBlocks.OAK_BARREL)).entries((displayContext, entries) -> {
                        entries.add(ModBlocks.ACACIA_BARREL);
                        entries.add(ModBlocks.BIRCH_BARREL);
                        entries.add(ModBlocks.CRIMSON_BARREL);
                        entries.add(ModBlocks.DARK_BARREL);
                        entries.add(ModBlocks.JUNGLE_BARREL);
                        entries.add(ModBlocks.OAK_BARREL);
                        entries.add(ModBlocks.SPRUCE_BARREL);
                        entries.add(ModBlocks.WARPED_BARREL);

                    }).build());
    public static void registerItemGroups() {
        Experience_Container.LOGGER.info("Registring item groups");

    }
}
