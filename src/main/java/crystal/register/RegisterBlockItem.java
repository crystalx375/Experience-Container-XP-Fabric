package crystal.register;

import crystal.ExperienceContainer;
import crystal.block.ExperienceContainerBlock;
import crystal.block.ExperienceContainerBlockItem;
import crystal.block.BlockEntityData;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static crystal.ExperienceContainer.MOD_ID;

public class RegisterBlockItem {
    private static final AbstractBlock.Settings SETTINGS = Blocks.BARREL.getSettings().strength(1.0f, 50.0f);

    public static final Block ACACIA_BARREL = addItemBlock("acacia_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block BIRCH_BARREL = addItemBlock("birch_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block CRIMSON_BARREL = addItemBlock("crimson_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block DARK_BARREL = addItemBlock("dark_oak_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block JUNGLE_BARREL = addItemBlock("jungle_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block OAK_BARREL = addItemBlock("oak_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block SPRUCE_BARREL = addItemBlock("spruce_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block WARPED_BARREL = addItemBlock("warped_small_box", new ExperienceContainerBlock(SETTINGS));

    public static final BlockEntityType<BlockEntityData> EXPERIENCE_BARRELS = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "experience_barrels"),
            BlockEntityType.Builder.create(BlockEntityData::new,
                    ACACIA_BARREL, BIRCH_BARREL, CRIMSON_BARREL, DARK_BARREL,
                    JUNGLE_BARREL, OAK_BARREL, SPRUCE_BARREL, WARPED_BARREL
            ).build(null)
    );

    private static Block addItemBlock(String id, Block block) {
        Identifier identifier = Identifier.of(MOD_ID, id);
        Registry.register(Registries.ITEM, identifier,
                new ExperienceContainerBlockItem(block, new Item.Settings()) {

        });
        return Registry.register(Registries.BLOCK, identifier, block);
    }

    private static void registerCreativeInventory() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.add(ACACIA_BARREL);
            content.add(BIRCH_BARREL);
            content.add(CRIMSON_BARREL);
            content.add(DARK_BARREL);
            content.add(JUNGLE_BARREL);
            content.add(OAK_BARREL);
            content.add(SPRUCE_BARREL);
            content.add(WARPED_BARREL);
        });
    }

    public static void init() {
        registerCreativeInventory();
        ExperienceContainer.LOGGER.info("Registering item & blocks...");
    }
}
