package crystal;

import crystal.register.RegisterBlockItem;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperienceContainer implements ModInitializer {
	public static final String MOD_ID = "experience-container";
	public static final Logger LOGGER = LoggerFactory.getLogger("Experience Container");

	@Override
	public void onInitialize() {
		LOGGER.info("Loading...");
        RegisterBlockItem.init();
	}
}
