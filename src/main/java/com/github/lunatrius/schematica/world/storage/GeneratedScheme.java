package com.github.lunatrius.schematica.world.storage;

import java.util.Map;

import com.github.lunatrius.schematica.world.storage.Schematic;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeneratedScheme extends Schematic {

	Map<BlockPos, IBlockState> blockStates;

	public GeneratedScheme(Map<BlockPos, IBlockState> blockStates, ItemStack icon, int width, int height, int length) {
		super(icon, width, height, length);

		this.blockStates = blockStates;
	}

	public Map<BlockPos, IBlockState> getBlockStatesMap() {
		return blockStates;
	}

	@Override
	public IBlockState getRequiredBlockState(BlockPos pos, World world) {
		IBlockState state = blockStates.get(pos);
		if(state == null)
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

}
