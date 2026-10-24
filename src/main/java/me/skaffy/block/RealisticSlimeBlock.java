package me.skaffy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

/** Slows entities down, like the original slime fluid did via its fluid class. */
public class RealisticSlimeBlock extends RealisticLiquidBlock {
	private static final Vec3 STUCK = new Vec3(0.45, 0.3, 0.45);

	public RealisticSlimeBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		entity.makeStuckInBlock(state, STUCK);
	}
}
