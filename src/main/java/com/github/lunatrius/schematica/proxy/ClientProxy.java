package com.github.lunatrius.schematica.proxy;

import java.io.File;
import java.io.IOException;

import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.core.util.vector.Vector3d;
import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.client.gui.control.GuiSchematicControl;
import com.github.lunatrius.schematica.client.gui.load.GuiSchematicLoad;
import com.github.lunatrius.schematica.client.gui.save.GuiSchematicSave;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.util.FlipHelper;
import com.github.lunatrius.schematica.client.util.RotationHelper;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.command.client.CommandSchematicaReplace;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.handler.client.GuiHandler;
import com.github.lunatrius.schematica.handler.client.InputHandler;
import com.github.lunatrius.schematica.handler.client.OverlayHandler;
import com.github.lunatrius.schematica.handler.client.RenderTickHandler;
import com.github.lunatrius.schematica.handler.client.TickHandler;
import com.github.lunatrius.schematica.handler.client.WorldHandler;
import com.github.lunatrius.schematica.reference.Reference;
import com.github.lunatrius.schematica.world.schematic.SchematicAlpha;
import com.github.lunatrius.schematica.world.schematic.SchematicFormat;

import aceart.api.Controlling;
import aceart.api.InitObject;
import aceart.api.Printable;
import aceart.api.Saving;
import aceart.api.ServerUpdater;
import aceart.schemes.MWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	public static boolean isRenderingGuide = false;
	public static boolean isPendingReset = false;

	public static final Vector3d playerPosition = new Vector3d();
	public static EnumFacing orientation = null;
	public static int rotationRender = 0;
	
	public static SchematicWorld schematic = null;
	public static MBlockPos pointA = new MBlockPos();
	public static MBlockPos pointB = new MBlockPos();
	public static final MBlockPos pointMin = new MBlockPos();
	public static final MBlockPos pointMax = new MBlockPos();

	public static final EnumFacing axisFlip = EnumFacing.NORTH;
	public static final EnumFacing axisRotation = EnumFacing.UP;

	public static RayTraceResult objectMouseOver = null;

	private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

	public static void setPlayerData(final EntityPlayer player, final float partialTicks) {
		playerPosition.x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		playerPosition.y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		playerPosition.z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

		orientation = getOrientation(player);

		rotationRender = MathHelper.floor(player.rotationYaw / 90) & 3;
	}

	private static EnumFacing getOrientation(final EntityPlayer player) {
		if (player.rotationPitch > 45) {
			return EnumFacing.DOWN;
		} else if (player.rotationPitch < -45) {
			return EnumFacing.UP;
		} else {
			switch (MathHelper.floor(player.rotationYaw / 90.0 + 0.5) & 3) {
			case 0:
				return EnumFacing.SOUTH;
			case 1:
				return EnumFacing.WEST;
			case 2:
				return EnumFacing.NORTH;
			case 3:
				return EnumFacing.EAST;
			}
		}

		return null;
	}

	public static void updatePoints() {
		pointMin.x = Math.min(pointA.x, pointB.x);
		pointMin.y = Math.min(pointA.y, pointB.y);
		pointMin.z = Math.min(pointA.z, pointB.z);

		pointMax.x = Math.max(pointA.x, pointB.x);
		pointMax.y = Math.max(pointA.y, pointB.y);
		pointMax.z = Math.max(pointA.z, pointB.z);
	}

	public static void resetPoint(final MBlockPos point) {
		
		BlockPos currentPos = saver.getCurrentPosition();
		point.x = (int) Math.floor(currentPos.getX());
		point.y = (int) Math.floor(currentPos.getY());
		point.z = (int) Math.floor(currentPos.getZ());

	}

	public static void setDisplace(BlockPos pos) {
		displace = new MBlockPos(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static boolean shouldDisplace = false;
	public static MBlockPos displace = new MBlockPos();
	public static void offsetSchematic(final SchematicWorld schematic) {
		if (schematic != null) {
			
			final MBlockPos position = schematic.position;

			BlockPos controllerPos = controller.getPosition();
			position.x = (int) Math.floor(controllerPos.getX());
			position.y = (int) Math.floor(controllerPos.getY());
			position.z = (int) Math.floor(controllerPos.getZ());
			
			if(shouldDisplace) 
				displaceSchematic(position);
			else
				indentSchematic(position);	
		}
	}
	
	private static void displaceSchematic(final MBlockPos position) {
		
		position.x -= displace.getX();
		position.y -= displace.getY();
		position.z -= displace.getZ();

	}

	private static void indentSchematic(final MBlockPos position) {
	 
		int indent = 1;
		switch (rotationRender) {
		case 0:
			position.x -= schematic.getWidth() - 1 + indent;
			position.z += indent;
			break;
		case 1:
			position.x -= schematic.getWidth() - 1 + indent;
			position.z -= schematic.getLength() - 1 + indent;
			break;
		case 2:
			position.x += indent;
			position.z -= schematic.getLength() - 1 + indent;
			break;
		case 3:
			position.x += indent;
			position.z += indent;
			break;
		}
	}
	
	private final SchematicFormat format = new SchematicAlpha();

	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

		final Property[] sliders = {
				ConfigurationHandler.propAlpha,
				ConfigurationHandler.propBlockDelta,
				ConfigurationHandler.propRenderDistance,
				ConfigurationHandler.propPlaceDelay,
				ConfigurationHandler.propTimeout,
				ConfigurationHandler.propPlaceDistance
		};
		for (final Property prop : sliders) {
			prop.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		}

		for (final KeyBinding keyBinding : InputHandler.KEY_BINDINGS) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}

	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		MinecraftForge.EVENT_BUS.register(InputHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(TickHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(RenderTickHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ConfigurationHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(RenderSchematic.INSTANCE);
		MinecraftForge.EVENT_BUS.register(GuiHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new OverlayHandler());
		MinecraftForge.EVENT_BUS.register(new WorldHandler());

		ClientCommandHandler.instance.registerCommand(new CommandSchematicaReplace());
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);

		resetSettings();
	}

	@Override
	public File getDataDirectory() {
		final File file = MINECRAFT.mcDataDir;
		try {
			return file.getCanonicalFile();
		} catch (final IOException e) {
			Reference.logger.debug("Could not canonize path!", e);
		}
		return file;
	}

	@Override
	public void resetSettings() {
		super.resetSettings();

		SchematicPrinter.INSTANCE.setEnabled(true);

		RenderSchematic.INSTANCE.refresh();
		unloadSchematic(true);

		isRenderingGuide = false;

		playerPosition.set(0, 0, 0);
		orientation = null;
		rotationRender = 0;

		pointA.set(0, 0, 0);
		pointB.set(0, 0, 0);
		updatePoints();
		
		controller = InitObject.controller;
		saver = InitObject.saver;
		
	
	}

	@Override
	public void unloadSchematic(boolean flag) {
		schematic = null;
		if(flag) RenderSchematic.INSTANCE.setWorldAndLoadRenderers(null);
		SchematicPrinter.INSTANCE.setSchematic(null);
	}

	@Override
	public boolean loadSchematicFromFile(final EntityPlayer player, 
			final File directory, final String filename) {

		final ISchematic schematic = SchematicFormat.readFromFile(directory, filename);
		if(schematic == null) {
			System.out.println("Scheme with this name doesn't exist!");
			return false;
		}
		
		schemeName = filename;
//		controller.switchMode();
//		controller.setRotationRender(rotationRender);
	
		loadSchematic(player, schematic);

		return true;
	}

	public static int rotations;
	public static boolean flip;
	public static String schemeName;
	
	@Override
	public void loadSchematicFromController(EntityPlayer player) {

		//NBTTagCompound tag = controller.getSchematic();
		//ISchematic scheme = format.readFromNBT(tag);

		rotationRender = controller.getRotationRender();
		displace = MWrapper.transformBlockPos(controller.getDisplacement());
		rotations = controller.getRotations();
		flip = controller.getFlip();
		schemeName = controller.getSchemeName();
		
		loadSchematicFromFile(player, ConfigurationHandler.schematicDirectory, schemeName);
		//loadSchematic(player, scheme);

	}

	public boolean loadSchematic(final EntityPlayer player, 
			final ISchematic schematic) {

		if (schematic == null) {
			return false;
		}

		final SchematicWorld world = new SchematicWorld(schematic);

		unloadSchematic(true);

		ClientProxy.schematic = world;
		RenderSchematic.INSTANCE.setWorldAndLoadRenderers(world);
		SchematicPrinter.INSTANCE.setSchematic(world);
		world.isRendering = true;

		if (world != null) {
			
			for(int i = 1; i <= rotations; i++)
				RotationHelper.INSTANCE.rotate(ClientProxy.schematic, ClientProxy.axisRotation, false);
			if(flip)
				FlipHelper.INSTANCE.flip(ClientProxy.schematic, ClientProxy.axisFlip, false);
			
			ClientProxy.offsetSchematic(world);
			
//			ISchematic schemeToSend = world.getSchematic();
		
			updater.updateServer(new MBlockPos(), schemeName,
					rotationRender, displace, 2);
			
			rotationRender = 0;
			displace = new MBlockPos();
			rotations = 0;
			flip = false;
		} 

		return true;
	}

	@Override
	public boolean isPlayerQuotaExceeded(final EntityPlayer player) {
		return false;
	}

	@Override
	public File getPlayerSchematicDirectory(final EntityPlayer player, final boolean privateDirectory) {
		return ConfigurationHandler.schematicDirectory;
	}

	@Override
	public void openGuiSave() {
		MINECRAFT.displayGuiScreen(new GuiSchematicSave(MINECRAFT.currentScreen));
	}

	@Override
	public void openGuiLoad() {
		MINECRAFT.displayGuiScreen(new GuiSchematicLoad(MINECRAFT.currentScreen));
	}

	@Override
	public void openGuiControl() {
		MINECRAFT.displayGuiScreen(new GuiSchematicControl(MINECRAFT.currentScreen));
	}
	

}
