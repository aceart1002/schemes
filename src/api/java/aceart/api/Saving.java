package aceart.api;

import net.minecraft.util.math.BlockPos;

public interface Saving {

	BlockPos getCurrentPosition();
	
	BlockPos getSavedPoint();
	
	void setSavedPoint(BlockPos pos);
}
