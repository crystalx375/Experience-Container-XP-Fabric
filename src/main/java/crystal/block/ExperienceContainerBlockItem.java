package crystal.block;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static crystal.util.Barrel.MAX_XP;

public class ExperienceContainerBlockItem extends BlockItem {

    public ExperienceContainerBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        final NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);

        if (nbtComponent != null && nbtComponent.contains("xp")) {
            final int xpFromBarrel = nbtComponent.copyNbt().getInt("xp");
            final float fillPercent = (float) xpFromBarrel / MAX_XP;
            final int filledBars = Math.round(fillPercent * 10);
            StringBuilder bar = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                bar.append(i < filledBars ? "░" : ".");
            }
            if (xpFromBarrel > 0) {
                tooltip.add(Text.translatable(
                        "tooltip.experience-container.has_xp",
                        Text.literal(
                                String.valueOf(1)
                        ).formatted(Formatting.YELLOW)
                ));
                tooltip.add(Text.literal( "§a[" + bar + "] " + (int)(fillPercent * 100) + "§a%"));
            } else {
                tooltip.add(Text.translatable("tooltip.experience-container.empty"));
            }
            super.appendTooltip(stack, context, tooltip, type);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}