package aceart.api;

import net.minecraft.util.math.BlockPos;

public interface ServerUpdater {
	
	void updateServer(BlockPos savePoint, String name, int rotationRender, BlockPos displacement, int operation);
	
}
