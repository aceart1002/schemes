package aceart.api;

import java.util.ArrayList;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface Printable {
	
	void construct(BlockPos referenceBlock, EnumFacing playerFacing, EnumFacing blockSide);
	
	void buildAreaToPrint();
	
	ArrayList<BlockPos> getBuiltArea();
	
}
