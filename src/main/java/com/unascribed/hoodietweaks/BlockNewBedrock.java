package com.unascribed.hoodietweaks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNewBedrock extends Block {

	public BlockNewBedrock() {
		super(Material.ROCK);
		setHardness(HoodieTweaks.inst.bedrockHardness);
		setSoundType(SoundType.STONE);
		setTranslationKey("bedrock");
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setResistance(6000000);
		for (String s : HoodieTweaks.inst.bedrockTools) {
			setHarvestLevel(s, HoodieTweaks.inst.bedrockHarvestLevel);
		}
		setRegistryName("minecraft", "bedrock");
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		if (pos.getY() == 0 && HoodieTweaks.inst.bedrockUnbreakableAtY0) return -1;
		return super.getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return HoodieTweaks.inst.bedrockDrops ? new ItemStack(Blocks.BEDROCK) : ItemStack.EMPTY;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return HoodieTweaks.inst.bedrockDrops && !HoodieTweaks.inst.bedrockDropsSilkTouchOnly ? Item.getItemFromBlock(Blocks.BEDROCK) : Items.AIR;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return HoodieTweaks.inst.bedrockDrops && !HoodieTweaks.inst.bedrockDropsSilkTouchOnly ? 1 : 0;
	}

}
