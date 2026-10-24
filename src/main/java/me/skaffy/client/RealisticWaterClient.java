package me.skaffy.client;

import me.skaffy.ModContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Port note: the original mod draws water with its own Vulkan renderer. This port only
 * registers the vanilla fluid renderer for the realistic fluids (tinted water/slime, vanilla lava).
 */
public class RealisticWaterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FluidRenderHandlerRegistry.INSTANCE.register(
			ModContent.REALISTIC_WATER, ModContent.FLOWING_REALISTIC_WATER,
			SimpleFluidRenderHandler.coloredWater(0x3F76E4));

		FluidRenderHandlerRegistry.INSTANCE.register(
			ModContent.REALISTIC_SLIME, ModContent.FLOWING_REALISTIC_SLIME,
			SimpleFluidRenderHandler.coloredWater(0x7BC74D));

		FluidRenderHandlerRegistry.INSTANCE.register(
			ModContent.REALISTIC_LAVA, ModContent.FLOWING_REALISTIC_LAVA,
			new SimpleFluidRenderHandler(
				new ResourceLocation("minecraft", "block/lava_still"),
				new ResourceLocation("minecraft", "block/lava_flow")));

		BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(),
			ModContent.REALISTIC_WATER, ModContent.FLOWING_REALISTIC_WATER,
			ModContent.REALISTIC_SLIME, ModContent.FLOWING_REALISTIC_SLIME);
	}
}
