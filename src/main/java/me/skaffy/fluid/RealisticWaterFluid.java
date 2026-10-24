package me.skaffy.fluid;

import java.util.Optional;
import me.skaffy.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

/**
 * Modified 1.20.6 port. Unlike the original, the fluid uses vanilla server-side flowing logic,
 * because the original simulation lives in the Vulkan client.
 */
public abstract class RealisticWaterFluid extends FlowingFluid {
	@Override
	public Fluid getFlowing() {
		return ModContent.FLOWING_REALISTIC_WATER;
	}

	@Override
	public Fluid getSource() {
		return ModContent.REALISTIC_WATER;
	}

	@Override
	public Item getBucket() {
		return ModContent.REALISTIC_WATER_BUCKET;
	}

	public static boolean isRealistic(FluidState state) {
		return !state.isEmpty() && state.getType() instanceof RealisticWaterFluid;
	}

	@Override
	public ParticleOptions getDripParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	public boolean canConvertToSource(Level level) {
		return false;
	}

	@Override
	public void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		Block.dropResources(state, level, pos, blockEntity);
	}

	@Override
	public int getSlopeFindDistance(LevelReader level) {
		return 4;
	}

	@Override
	public BlockState createLegacyBlock(FluidState fluidState) {
		return ModContent.REALISTIC_WATER_BLOCK.defaultBlockState()
			.setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
	}

	@Override
	public boolean isSame(Fluid other) {
		return other == ModContent.REALISTIC_WATER || other == ModContent.FLOWING_REALISTIC_WATER;
	}

	@Override
	public int getDropOff(LevelReader level) {
		return 1;
	}

	@Override
	public int getTickDelay(LevelReader level) {
		return 5;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
		return direction == Direction.DOWN && !other.is(FluidTags.WATER);
	}

	@Override
	public float getExplosionResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL);
	}

	public static class Flowing extends RealisticWaterFluid {
		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState fluidState) {
			return fluidState.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return false;
		}
	}

	public static class Source extends RealisticWaterFluid {
		@Override
		public int getAmount(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return true;
		}
	}
}
