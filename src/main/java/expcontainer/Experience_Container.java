package expcontainer;

import expcontainer.block.ExperienceContainerBlock;
import expcontainer.block.ModBlocks;
import expcontainer.block.entity.ExperienceContainerBlockEntity;
import expcontainer.block.entity.ExperienceContainerBlockEntityType;
import expcontainer.item.ModItemGroup;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Experience_Container implements ModInitializer {
	public static final String MOD_ID = "expcontainer";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        LOGGER.info("Starting " + Experience_Container.MOD_ID);
        ModBlocks.registerModBlocks();
        ModItemGroup.registerItemGroups();
        ExperienceContainerBlockEntityType.registerAll();
        ExperienceContainerBlockEntity.registerNBT();
        ExperienceContainerBlock.registerBlock();
	}
}