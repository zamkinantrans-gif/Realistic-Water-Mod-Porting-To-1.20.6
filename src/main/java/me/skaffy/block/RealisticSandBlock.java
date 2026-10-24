package me.skaffy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Layered sand. NOTE (port change): in the original the sand is drawn and simulated by the
 * Vulkan client and has no collision. In this port it is a normal visible block with collision.
 */
public class RealisticSandBlock extends Block {
	public static final int MAX_LAYERS = 8;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	private static final VoxelShape[] SHAPES = new VoxelShape[MAX_LAYERS + 1];

	static {
		for (int layers = 0; layers <= MAX_LAYERS; layers++) {
			SHAPES[layers] = Block.box(0.0, 0.0, 0.0, 16.0, Math.max(layers, 1) * 2.0, 16.0);
		}
	}

	public RealisticSandBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, MAX_LAYERS));
	}

	public static int layers(BlockState state) {
		return state.getBlock() instanceof RealisticSandBlock ? state.getValue(LAYERS) : 0;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (context.getItemInHand().is(this.asItem())) {
			return state.getValue(LAYERS) < MAX_LAYERS;
		}
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}
}
