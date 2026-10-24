package me.skaffy.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

/** Thin public wrapper, because LiquidBlock's constructor is not public. */
public class RealisticLiquidBlock extends LiquidBlock {
	public RealisticLiquidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}
}
