package aceart.blocks.tiles;

import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.proxy.ClientProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSavePoints extends TileEntity {

	private int coorX = 0;
	private int coorY = 0;
	private int coorZ = 0;
	
	public MBlockPos getPoint() {
		return new MBlockPos(coorX, coorY, coorZ);
	}

	public void setPoint(MBlockPos point) {
		coorX = point.getX();
		coorY = point.getY();
		coorZ = point.getZ();
		markDirty();
	}
	
	public void setPoint(BlockPos point) {
		coorX = point.getX();
		coorY = point.getY();
		coorZ = point.getZ();
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompoundTakingInformation) {
		
		tagCompoundTakingInformation.setInteger("coorX", coorX);
		tagCompoundTakingInformation.setInteger("coorY", coorY);
		tagCompoundTakingInformation.setInteger("coorZ", coorZ);

		return super.writeToNBT(tagCompoundTakingInformation);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompoundGivingInformation) {

		coorX = tagCompoundGivingInformation.getInteger("coorX");
		coorY = tagCompoundGivingInformation.getInteger("coorY");
		coorZ = tagCompoundGivingInformation.getInteger("coorZ");
		
		super.readFromNBT(tagCompoundGivingInformation);
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
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
}
