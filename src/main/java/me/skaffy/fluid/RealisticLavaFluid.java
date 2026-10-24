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
import me.skaffy.ModSettings;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;

/**
 * Modified 1.20.6 port. Unlike the original, the fluid uses vanilla server-side flowing logic,
 * because the original simulation lives in the Vulkan client.
 */
public abstract class RealisticLavaFluid extends FlowingFluid {
	@Override
	public Fluid getFlowing() {
		return ModContent.FLOWING_REALISTIC_LAVA;
	}

	@Override
	public Fluid getSource() {
		return ModContent.REALISTIC_LAVA;
	}

	@Override
	public Item getBucket() {
		return ModContent.REALISTIC_LAVA_BUCKET;
	}

	public static boolean isRealistic(FluidState state) {
		return !state.isEmpty() && state.getType() instanceof RealisticLavaFluid;
	}

	@Override
	public ParticleOptions getDripParticle() {
		return ParticleTypes.DRIPPING_LAVA;
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
		return ModContent.REALISTIC_LAVA_BLOCK.defaultBlockState()
			.setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
	}

	@Override
	public boolean isSame(Fluid other) {
		return other == ModContent.REALISTIC_LAVA || other == ModContent.FLOWING_REALISTIC_LAVA;
	}

	@Override
	public int getDropOff(LevelReader level) {
		return 2;
	}

	@Override
	public int getTickDelay(LevelReader level) {
		return 30;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
		return direction == Direction.DOWN && !other.is(FluidTags.LAVA);
	}

	@Override
	public float getExplosionResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
	}

	@Override
	public void randomTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
		if (!ModSettings.lavaIgniteFlammables || !level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
			return;
		}
		int passes = random.nextInt(3);
		if (passes > 0) {
			BlockPos testPos = pos;
			for (int pass = 0; pass < passes; pass++) {
				testPos = testPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
				if (!level.isLoaded(testPos)) {
					return;
				}
				BlockState blockState = level.getBlockState(testPos);
				if (blockState.isAir()) {
					if (this.hasFlammableNeighbours(level, testPos)) {
						level.setBlockAndUpdate(testPos, BaseFireBlock.getState(level, testPos));
						return;
					}
				} else if (blockState.blocksMotion()) {
					return;
				}
			}
		} else {
			for (int i = 0; i < 3; i++) {
				BlockPos testPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
				if (!level.isLoaded(testPos)) {
					return;
				}
				if (level.isEmptyBlock(testPos.above()) && this.isFlammable(level, testPos)) {
					level.setBlockAndUpdate(testPos.above(), BaseFireBlock.getState(level, testPos));
				}
			}
		}
	}

	@Override
	public boolean isRandomlyTicking() {
		return true;
	}

	private boolean hasFlammableNeighbours(LevelReader level, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			if (this.isFlammable(level, pos.relative(direction))) {
				return true;
			}
		}
		return false;
	}

	private boolean isFlammable(LevelReader level, BlockPos pos) {
		return !level.isOutsideBuildHeight(pos.getY()) && level.hasChunkAt(pos) && level.getBlockState(pos).ignitedByLava();
	}

	public static class Flowing extends RealisticLavaFluid {
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

	public static class Source extends RealisticLavaFluid {
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
