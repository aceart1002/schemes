package aceart.blocks.tiles;

import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.world.schematic.SchematicAlpha;
import com.github.lunatrius.schematica.world.schematic.SchematicFormat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSchemeContainer extends TileEntity {
	
	private static final SchematicFormat FORMAT = new SchematicAlpha();
	
	String operationMode = "load";
	private NBTTagCompound schemeTag = new NBTTagCompound();
	
	String schemeName = "";
	
	private int rotationRender = 0;
	
	public String getName() {
		return schemeName;
	}
	
	public void setName(String newName) {
		schemeName = newName;
		markDirty();
	}
	
	public final int getRotations() {
		return rotations;
	}

	public final void setRotations(int rotations) {
		this.rotations = rotations;
		markDirty();
	}

	public final boolean getFlip() {
		return flip;
	}

	public final void setFlip(boolean flip) {
		this.flip = flip;
		markDirty();
	}

	private int rotations = 0;
	private boolean flip = false;
	
	private int dX = 0;
	private int dY = 0;
	private int dZ = 0;
	
	public BlockPos getDislacement() {
		return new BlockPos(dX, dY, dZ);
	}
	
	public void setDisplaceCoords(BlockPos pos) {
		dX = pos.getX();
		dY = pos.getY();
		dZ = pos.getZ();
		
		markDirty();
	}
	
	public int getRotationRender() {
		return rotationRender;
	}

	public void setRotationRender(int rotationRender) {
		this.rotationRender = rotationRender;
		markDirty();
	}

	public void setSchematic(NBTTagCompound schemeCompound) {
//		writeSchemeToTag(schematic);
		schemeTag = schemeCompound;
		markDirty();
	}
	
	public NBTTagCompound getSchematic() {
		return schemeTag;
	}
	
	
	public void switchOperationMode() {
		operationMode = "control";
		markDirty();
	}
	
	public String getOperationMode() {
		return operationMode;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompoundTakingScheme) {
		
		tagCompoundTakingScheme.setInteger("direction", rotationRender);
		tagCompoundTakingScheme.setInteger("rotations", rotations);
		tagCompoundTakingScheme.setBoolean("flip", flip);
		tagCompoundTakingScheme.setString("mode", operationMode);
		tagCompoundTakingScheme.setTag("scheme", schemeTag);
		tagCompoundTakingScheme.setString("schemeName", schemeName);
		
		
		
		tagCompoundTakingScheme.setInteger("dX", dX);
		tagCompoundTakingScheme.setInteger("dY", dY);
		tagCompoundTakingScheme.setInteger("dZ", dZ);

		return super.writeToNBT(tagCompoundTakingScheme);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompoundGivingScheme) {
		
		rotationRender = tagCompoundGivingScheme.getInteger("direction");
		rotations = tagCompoundGivingScheme.getInteger("rotations");
		flip = tagCompoundGivingScheme.getBoolean("flip");
		operationMode = tagCompoundGivingScheme.getString("mode");
		schemeTag = tagCompoundGivingScheme.getCompoundTag("scheme");
		schemeName = tagCompoundGivingScheme.getString("schemeName");
		
		dX = tagCompoundGivingScheme.getInteger("dX");
		dY = tagCompoundGivingScheme.getInteger("dY");
		dZ = tagCompoundGivingScheme.getInteger("dZ");
		
		super.readFromNBT(tagCompoundGivingScheme);
		
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();//getUpdateTag();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
