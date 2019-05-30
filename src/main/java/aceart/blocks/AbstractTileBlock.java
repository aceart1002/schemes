package aceart.blocks;

import javax.annotation.Nullable;

import aceart.blocks.tiles.ContainsTile;
import aceart.registry.Registrable;
import aceart.schemes.Schemes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class AbstractTileBlock<T extends TileEntity> extends Block 
implements Registrable, ContainsTile {
	
	String name;
	
	public AbstractTileBlock(Material material, String name) {
		super(material);
		
		this.name = name;
		
		setCreativeTab(Schemes.SCHEMES);
		
	}

	@Override
	public String getCustomRegistryName() {
		return name;
	}

	@Override
	public void setCustomRegistryName(String newName) {
		name = newName;
		
	}

	public abstract Class<T> getTileEntityClass();

	public T getTileEntity(IBlockAccess world, BlockPos position) {

		return (T) world.getTileEntity(position);
	}

	@Override
	public boolean hasTileEntity(IBlockState blockState) {

		return true;
	}

	@Nullable
	@Override
	public abstract T createTileEntity(World world, IBlockState blockState);
}
