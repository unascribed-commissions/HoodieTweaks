package com.unascribed.squakedonequick;

import net.minecraft.block.BlockHay;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHayNoFall extends BlockHay {

	public BlockHayNoFall() {
		setHardness(0.5F);
		setSoundType(SoundType.PLANT);
		setTranslationKey("hayBlock");
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setRegistryName("minecraft", "hay_block");
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// do nothing
	}

}
