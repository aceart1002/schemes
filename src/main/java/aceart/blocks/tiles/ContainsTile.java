package aceart.blocks.tiles;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface ContainsTile<T extends TileEntity> {

	default public T getTileEntity(IBlockAccess world, BlockPos position) {

		return (T) world.getTileEntity(position);
	}

	default public boolean hasTileEntity(IBlockState blockState) {

		return true;
	}

	public abstract Class<T> getTileEntityClass();
	
	@Nullable
	public abstract T createTileEntity(World world, IBlockState blockState);
	

}
