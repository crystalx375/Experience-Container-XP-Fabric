package crystal.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.util.Identifier;

import static crystal.register.RegisterBlockItem.*;

public class ExperienceContainerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        setBarrel(ACACIA_BARREL);
        setBarrel(BIRCH_BARREL);
        setBarrel(CHERRY_BARREL);
        setBarrel(CRIMSON_BARREL);
        setBarrel(DARK_BARREL);
        setBarrel(JUNGLE_BARREL);
        setBarrel(OAK_BARREL);
        setBarrel(SPRUCE_BARREL);
        setBarrel(WARPED_BARREL);
	}

    private static void setBarrel(Block block) {
        ModelPredicateProviderRegistry.register(
                block.asItem(),
                Identifier.of("experience-container", "active"),
                (stack, world, entity, seed) -> {
                    final NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
                    if (nbtComponent != null) {
                        final boolean active = nbtComponent.copyNbt().getBoolean("active");
                        if (active) return 1;
                        return 0;
                    }
                    return 0;
                }
        );
    }
}