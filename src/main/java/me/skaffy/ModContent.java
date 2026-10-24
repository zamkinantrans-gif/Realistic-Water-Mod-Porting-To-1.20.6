package me.skaffy;

import me.skaffy.block.RealisticLiquidBlock;
import me.skaffy.block.RealisticSandBlock;
import me.skaffy.block.RealisticSlimeBlock;
import me.skaffy.fluid.RealisticLavaFluid;
import me.skaffy.fluid.RealisticSlimeFluid;
import me.skaffy.fluid.RealisticWaterFluid;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModContent {
	// ---- Water ----
	public static final FlowingFluid FLOWING_REALISTIC_WATER = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("flowing_realistic_water"), new RealisticWaterFluid.Flowing());
	public static final FlowingFluid REALISTIC_WATER = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("realistic_water"), new RealisticWaterFluid.Source());

	public static final Block REALISTIC_WATER_BLOCK = Registry.register(BuiltInRegistries.BLOCK, RealisticWater.id("realistic_water"),
		new RealisticLiquidBlock(REALISTIC_WATER, liquidProperties(MapColor.WATER)));

	public static final Item REALISTIC_WATER_BUCKET = Registry.register(BuiltInRegistries.ITEM, RealisticWater.id("realistic_water_bucket"),
		new BucketItem(REALISTIC_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

	// ---- Lava ----
	public static final FlowingFluid FLOWING_REALISTIC_LAVA = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("flowing_realistic_lava"), new RealisticLavaFluid.Flowing());
	public static final FlowingFluid REALISTIC_LAVA = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("realistic_lava"), new RealisticLavaFluid.Source());

	public static final Block REALISTIC_LAVA_BLOCK = Registry.register(BuiltInRegistries.BLOCK, RealisticWater.id("realistic_lava"),
		new RealisticLiquidBlock(REALISTIC_LAVA, liquidProperties(MapColor.FIRE).lightLevel(state -> 15)));

	public static final Item REALISTIC_LAVA_BUCKET = Registry.register(BuiltInRegistries.ITEM, RealisticWater.id("realistic_lava_bucket"),
		new BucketItem(REALISTIC_LAVA, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

	// ---- Slime ----
	public static final FlowingFluid FLOWING_REALISTIC_SLIME = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("flowing_realistic_slime"), new RealisticSlimeFluid.Flowing());
	public static final FlowingFluid REALISTIC_SLIME = Registry.register(
		BuiltInRegistries.FLUID, RealisticWater.id("realistic_slime"), new RealisticSlimeFluid.Source());

	public static final Block REALISTIC_SLIME_BLOCK = Registry.register(BuiltInRegistries.BLOCK, RealisticWater.id("realistic_slime"),
		new RealisticSlimeBlock(REALISTIC_SLIME, liquidProperties(MapColor.GRASS)));

	public static final Item REALISTIC_SLIME_BUCKET = Registry.register(BuiltInRegistries.ITEM, RealisticWater.id("realistic_slime_bucket"),
		new BucketItem(REALISTIC_SLIME, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)) {
			@Override
			protected void playEmptySound(Player player, LevelAccessor level, BlockPos pos) {
				level.playSound(player, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
			}
		});

	// ---- Sand ----
	public static final Block REALISTIC_SAND = Registry.register(BuiltInRegistries.BLOCK, RealisticWater.id("realistic_sand"),
		new RealisticSandBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.SAND)
			.instrument(NoteBlockInstrument.SNARE)
			.strength(0.5F)
			.sound(SoundType.SAND)
			.noOcclusion()
			.pushReaction(PushReaction.DESTROY)));

	public static final Item REALISTIC_SAND_ITEM = registerBlockItem("realistic_sand", REALISTIC_SAND);

	private ModContent() {
	}

	private static BlockBehaviour.Properties liquidProperties(MapColor color) {
		return BlockBehaviour.Properties.of()
			.mapColor(color)
			.replaceable()
			.noCollission()
			.strength(100.0F)
			.pushReaction(PushReaction.DESTROY)
			.noLootTable()
			.liquid()
			.sound(SoundType.EMPTY);
	}

	private static Item registerBlockItem(String path, Block block) {
		BlockItem item = new BlockItem(block, new Item.Properties());
		item.registerBlocks(Item.BY_BLOCK, item);
		return Registry.register(BuiltInRegistries.ITEM, RealisticWater.id(path), item);
	}

	static void init() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.addAfter(Items.WATER_BUCKET, REALISTIC_WATER_BUCKET);
			entries.addAfter(Items.LAVA_BUCKET, REALISTIC_LAVA_BUCKET);
			entries.addAfter(REALISTIC_LAVA_BUCKET, REALISTIC_SLIME_BUCKET);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
			.register(entries -> entries.addAfter(Items.SAND, REALISTIC_SAND_ITEM));
	}
}
