package aceart.api;

import com.github.lunatrius.core.util.math.MBlockPos;

import net.minecraft.util.math.BlockPos;

public class MWrapper {
	public static MBlockPos transformBlockPos(BlockPos pos) {
		return new MBlockPos(pos.getX(), pos.getY(), pos.getZ());
	}
}
