package expcontainer.block.entity;

import expcontainer.Experience_Container;
import expcontainer.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ExperienceContainerBlockEntityType {

    public static BlockEntityType<ExperienceContainerBlockEntity> EXPERIENCE_CONTAINER_BLOCK_ENTITY;

    public static void registerAll() {
        EXPERIENCE_CONTAINER_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Experience_Container.MOD_ID, "experience_container_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        ExperienceContainerBlockEntity::new,
                        ModBlocks.ACACIA_BARREL,
                        ModBlocks.BIRCH_BARREL,
                        ModBlocks.CRIMSON_BARREL,
                        ModBlocks.DARK_BARREL,
                        ModBlocks.JUNGLE_BARREL,
                        ModBlocks.OAK_BARREL,
                        ModBlocks.SPRUCE_BARREL,
                        ModBlocks.WARPED_BARREL
                ).build()
        );

        Experience_Container.LOGGER.info("Registering ExperienceContainerBlockEntityType");
    }
}
