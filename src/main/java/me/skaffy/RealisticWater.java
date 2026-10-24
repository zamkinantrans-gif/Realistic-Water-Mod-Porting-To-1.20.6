package me.skaffy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modified 1.20.6 port of Realistic Water by Skaffy.
 * Original project: https://github.com/sskaffy/Realistic-Water
 */
public class RealisticWater implements ModInitializer {
	public static final String MOD_ID = "realistic-water";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModContent.init();
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
