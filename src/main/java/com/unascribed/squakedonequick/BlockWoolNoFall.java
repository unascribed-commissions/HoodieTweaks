package com.unascribed.squakedonequick;

import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWoolNoFall extends BlockColored {

	public BlockWoolNoFall() {
		super(Material.CLOTH);
		setHardness(0.8f);
		setSoundType(SoundType.CLOTH);
		setTranslationKey("cloth");
		setRegistryName("minecraft", "wool");
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// do nothing
	}

}
