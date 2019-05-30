package aceart.blocks;

import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.proxy.ClientProxy;

import aceart.GuiTypes;
import aceart.blocks.AbstractTileBlock;
import aceart.blocks.tiles.TileSchemeContainer;
import aceart.schemes.Schemes;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSchemeController extends AbstractTileBlock<TileSchemeContainer> {

	private BlockData currentData;

	public BlockSchemeController(Material material, String name) {
		super(material, name);
	}
	
	private boolean isSameController(BlockPos currentPos) {

		BlockPos otherPos = null;
		if(ClientProxy.controller != null)
			otherPos = ClientProxy.controller.getPosition();
		else 
			return false;

		int x1 = currentPos.getX();
		int y1 = currentPos.getY();
		int z1 = currentPos.getZ();

		int x2 = otherPos.getX();
		int y2 = otherPos.getY();
		int z2 = otherPos.getZ();

		boolean poses = (x1 == x2 && y1 == y2 && z1 == z2) ? true : false;

		if(poses && ClientProxy.schematic != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		if(!isSameController(position)) {
			Schemes.proxy.unloadSchematic(false);
		}
		
		currentData = new BlockData(getTileEntity(world, position), position, world, 
				displaceOrNot());
		
		ClientProxy.controller = this;
		
		if (world.isRemote) {

			ClientProxy.shouldDisplace = displaceOrNot();
			
			String operationMode = getCurrentMode();

			if(operationMode.equals("load")) {
				openLoadingGui();
//				Schematica.proxy.openGuiLoad();

			}
			else if (operationMode.equals("control"))
				openControllingGui();
//				Schematica.proxy.openGuiControl();
			//			player.openGui(Schematica.instance, GuiTypes.TYPE.ordinal(), world, position.getX(), 
			//					position.getY(), position.getZ());
		}
		return true;
	}
	
	protected boolean displaceOrNot() {
		return false;
	}
	
	public void openLoadingGui() {
		Schemes.proxy.openGuiLoad();
	}
	
	public void openControllingGui() {
		Schemes.proxy.openGuiControl();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		
		try {
			if(isSameController(pos)) {
				Schemes.proxy.unloadSchematic(false);
			}
		} catch (Exception e) {
			
		}
		super.breakBlock(worldIn, pos, state);
	}

	public void switchMode() {
		currentData.getTile().switchOperationMode();
		updateTile();
	}

	public String getCurrentMode() {
		return currentData.getTile().getOperationMode();
	}

	public NBTTagCompound getSchematic() {
		return currentData.getTile().getSchematic();
	}

	public void setRotationRender(int rotationRender) {
		currentData.getTile().setRotationRender(rotationRender);
		updateTile();
	}

	public int getRotationRender() {
		return currentData.getTile().getRotationRender();
	}

	public void setSchematic(NBTTagCompound scheme) {
		
		currentData.getTile().setSchematic(scheme);
		
		
	}
	
	public void updateTile() {
		World world = currentData.currentWorld;
		BlockPos pos = currentData.currentPos;
		IBlockState state = world.getBlockState(pos); 
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	public BlockPos getPosition() {
		return currentData.getPosition();
	}
	
	public MBlockPos getDisplacement() {
		BlockPos d = currentData.getTile().getDislacement();
		return new MBlockPos(d.getX(),d.getY(),d.getZ());
	}
	
	public void setDisplacement(MBlockPos pos) {
		currentData.getTile().setDisplaceCoords(pos);
		updateTile();
	}
	
	public int getRotations() {
		return currentData.getTile().getRotations();
	}
	
	public void switchRotations() {
		int rots = currentData.getTile().getRotations();
		
		rots++;
		if(rots > 3)
			rots = 0;
		
		currentData.getTile().setRotations(rots);
		updateTile();
	}
	
	public boolean getFlip() {
		return currentData.getTile().getFlip();
	}
	
	public void switchFlip() {
		boolean flip = currentData.getTile().getFlip() == true ? false : true;
		currentData.getTile().setFlip(flip);
		updateTile();
	}
	
	public void setSchemeName(String name) {
		currentData.getTile().setName(name);
		updateTile();
	}
	
	public String getSchemeName() {
		return currentData.getTile().getName();
	}

	@Override
	public Class<TileSchemeContainer> getTileEntityClass() {

		return TileSchemeContainer.class;
	}

	@Override
	public TileSchemeContainer createTileEntity(World world, IBlockState blockState) {

		return new TileSchemeContainer();
	}

	protected class BlockData {
		private final TileSchemeContainer currentTile;
		private final BlockPos currentPos;
		private final World currentWorld;
		private final boolean shouldDisplace;
		
		public BlockData(TileSchemeContainer tile, BlockPos pos, World world, boolean displace) {
			currentTile = tile;
			currentPos = pos;
			currentWorld = world;
			shouldDisplace = displace;
		}

		private TileSchemeContainer getTile() {
			return currentTile;
		}

		private BlockPos getPosition() {
			return currentPos;
		}
		
		private boolean shouldDisplace() {
			return shouldDisplace;
		}
	}

	

}

