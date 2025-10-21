package expcontainer.block;

import expcontainer.Experience_Container;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


// Blocks
public class ModBlocks {
    public static final Block.Settings BARREL_SETTINGS = FabricBlockSettings.copyOf(Blocks.BARREL)
            .instrument(Instrument.BASS)
            .strength(1.4f, 10);


    public static final Block ACACIA_BARREL = registerBlock("acacia_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block BIRCH_BARREL = registerBlock("birch_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block CRIMSON_BARREL = registerBlock("crimson_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block DARK_BARREL = registerBlock("dark_oak_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block JUNGLE_BARREL = registerBlock("jungle_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block OAK_BARREL = registerBlock("oak_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block SPRUCE_BARREL = registerBlock("spruce_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));
    public static final Block WARPED_BARREL = registerBlock("warped_small_box",
            new ExperienceContainerBlock(BARREL_SETTINGS));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Experience_Container.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Experience_Container.MOD_ID, name),
                new BlockItem(block, new Item.Settings()) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        int xp = 0;
                        if (stack.hasNbt() && stack.getSubNbt("BlockEntityTag") != null) {
                            xp = stack.getSubNbt("BlockEntityTag").getShort("StoredXp");
                        }

                        int level = expcontainer.block.ExperienceContainerBlock.getLevelFromExperience(xp);
                        int maxXp = 1628;
                        float fillPercent = (float) xp / maxXp;
                        int filledBars = Math.round(fillPercent * 10);
                        StringBuilder bar = new StringBuilder();

                        for (int i = 0; i < 10; i++) {
                            bar.append(i < filledBars ? "█" : "░");
                        }

                        if (xp > 0) {
                            tooltip.add(Text.translatable("tooltip.expcontainer.has_xp", xp, level));
                            tooltip.add(Text.literal("§a[" + bar + "] " + (int)(fillPercent * 100) + "%"));
                        } else {
                            tooltip.add(Text.translatable("tooltip.expcontainer.empty"));
                            tooltip.add(Text.literal("§7[░░░░░░░░░░] 0%"));
                        }

                        super.appendTooltip(stack, world, tooltip, context);
                    }
                });
    }
    public static void registerModBlocks() {
        Experience_Container.LOGGER.info("Registering blocks");
    }
}
