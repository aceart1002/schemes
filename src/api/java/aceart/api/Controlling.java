package aceart.api;

import com.github.lunatrius.core.util.math.MBlockPos;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface Controlling {

	String getSchemeName();
	
	void switchMode();

	String getCurrentMode();

	NBTTagCompound getSchematic();

	void setRotationRender(int rotationRender);

	int getRotationRender();

	void setSchematic(NBTTagCompound scheme);

	BlockPos getPosition();

	BlockPos getDisplacement();

	void setDisplacement(BlockPos pos);

	int getRotations();

	void switchRotations();

	boolean getFlip();

	void switchFlip();

	void setSchemeName(String name);

	
	
}
