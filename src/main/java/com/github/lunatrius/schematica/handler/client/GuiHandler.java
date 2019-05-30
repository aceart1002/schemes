package com.github.lunatrius.schematica.handler.client;

import com.github.lunatrius.schematica.client.gui.control.GuiSchematicControl;
import com.github.lunatrius.schematica.client.gui.load.GuiSchematicLoad;
import com.github.lunatrius.schematica.client.gui.save.GuiSchematicSave;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;

import aceart.GuiTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final GuiHandler INSTANCE = new GuiHandler();

	@SubscribeEvent
	public void onGuiOpen(final GuiOpenEvent event) {
		if (SchematicPrinter.INSTANCE.isPrinting()) {
			if (event.getGui() instanceof GuiEditSign) {
				event.setGui(null);
			}
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		Minecraft game = Minecraft.getMinecraft();

		if(ID == GuiTypes.SAVE.ordinal()) {
			return new GuiSchematicSave(game.currentScreen);
		} else if (ID == GuiTypes.LOAD.ordinal()) {
			return new GuiSchematicLoad(game.currentScreen);
		} else if (ID == GuiTypes.CONTROL.ordinal()) {
			return new GuiSchematicControl(game.currentScreen);
		}

		return null;
	}

}

