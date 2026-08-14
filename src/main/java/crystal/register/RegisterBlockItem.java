package crystal.register;

import crystal.ExperienceContainer;
import crystal.block.BlockEntityData;
import crystal.block.ExperienceContainerBlock;
import crystal.block.ExperienceContainerBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static crystal.ExperienceContainer.MOD_ID;

public class RegisterBlockItem {
    private static final AbstractBlock.Settings SETTINGS = Blocks.BARREL.getSettings().strength(1.0f, 50.0f);

    public static final Block ACACIA_BARREL = addItemBlock("acacia_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block BAMBOO_BARREL = addItemBlock("bamboo_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block BIRCH_BARREL = addItemBlock("birch_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block CHERRY_BARREL = addItemBlock("cherry_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block CRIMSON_BARREL = addItemBlock("crimson_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block DARK_BARREL = addItemBlock("dark_oak_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block JUNGLE_BARREL = addItemBlock("jungle_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block MANGROVE_BARREL = addItemBlock("mangrove_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block OAK_BARREL = addItemBlock("oak_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block SPRUCE_BARREL = addItemBlock("spruce_small_box", new ExperienceContainerBlock(SETTINGS));
    public static final Block WARPED_BARREL = addItemBlock("warped_small_box", new ExperienceContainerBlock(SETTINGS));

    public static final BlockEntityType<BlockEntityData> EXPERIENCE_BARRELS = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "experience_barrels"),
            BlockEntityType.Builder.create(BlockEntityData::new,
                    ACACIA_BARREL,
                    BAMBOO_BARREL,
                    BIRCH_BARREL,
                    CHERRY_BARREL,
                    CRIMSON_BARREL,
                    DARK_BARREL,
                    JUNGLE_BARREL,
                    MANGROVE_BARREL,
                    OAK_BARREL,
                    SPRUCE_BARREL,
                    WARPED_BARREL
            ).build(null)
    );

    private static Block addItemBlock(String id, Block block) {
        Identifier identifier = Identifier.of(MOD_ID, id);
        Registry.register(Registries.ITEM, identifier,
                new ExperienceContainerBlockItem(block, new Item.Settings()) {

        });
        return Registry.register(Registries.BLOCK, identifier, block);
    }

    public static void init() {
        NewItemGroup.init();
        ExperienceContainer.LOGGER.info("Registering items & blocks");
    }
}
