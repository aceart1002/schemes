package aceart.network;

import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.world.schematic.SchematicAlpha;
import com.github.lunatrius.schematica.world.schematic.SchematicFormat;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateMessage implements IMessage {

	private static final SchematicFormat FORMAT = new SchematicAlpha();

	String schemeName;
	public int rotationRender;

	public Vec3d displacement;

	//public NBTTagCompound schemeTag = new NBTTagCompound();
	public Vec3d point;
	

	public int operation;

	MBlockPos getPoint() {
		return new MBlockPos(point);
	}

	MBlockPos getDisplacement() {
		return new MBlockPos(displacement);
	}
	
	public UpdateMessage() {}

	public UpdateMessage(MBlockPos point, String name, int rotationRender, MBlockPos displacement, int operation) {
		this.schemeName = name;
		this.rotationRender = rotationRender;
		
		//FORMAT.writeToNBT(schemeTag, scheme);

		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		this.point = new Vec3d(x,y,z);

		double x1 = displacement.getX();
		double y1 = displacement.getY();
		double z1 = displacement.getZ();

		this.displacement = new Vec3d(x1,y1,z1);

		this.operation = operation;
	}


	@Override
	public void fromBytes(ByteBuf buf) {

		schemeName = ByteBufUtils.readUTF8String(buf);
		rotationRender = buf.readInt();
//		rotations = buf.readInt();
//		flip = buf.readBoolean();
		
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		point = new Vec3d(x,y,z);

		double x1 = buf.readDouble();
		double y1 = buf.readDouble();
		double z1 = buf.readDouble();
		displacement = new Vec3d(x1,y1,z1);

		//schemeTag = ByteBufUtils.readTag(buf);

		operation = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {

		ByteBufUtils.writeUTF8String(buf, schemeName);
		buf.writeInt(rotationRender);
//		buf.writeInt(rotations);
//		buf.writeBoolean(flip);

		buf.writeDouble(point.x);
		buf.writeDouble(point.y);
		buf.writeDouble(point.z);

		buf.writeDouble(displacement.x);
		buf.writeDouble(displacement.y);
		buf.writeDouble(displacement.z);

		//ByteBufUtils.writeTag(buf, schemeTag);

		buf.writeInt(operation);
	}



}
