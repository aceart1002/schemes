package com.github.lunatrius.schematica.client.gui.control;

import java.io.IOException;

import com.github.lunatrius.core.client.gui.GuiNumericField;
import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.util.FlipHelper;
import com.github.lunatrius.schematica.client.util.RotationHelper;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.client.world.SchematicWorld.LayerMode;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.reference.Constants;
import com.github.lunatrius.schematica.reference.Names;

import aceart.schemes.Schemes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;

public class GuiSchematicControl extends GuiScreenBase {
	private final SchematicWorld schematic;
	private final SchematicPrinter printer;

	private int centerX = 0;
	private int centerY = 0;

	private GuiNumericField numericX = null;
	private GuiNumericField numericY = null;
	private GuiNumericField numericZ = null;

	private GuiButton btnUnload = null;
	private GuiButton btnLayerMode = null;
	private GuiNumericField nfLayer = null;

	private GuiButton btnHide = null;
	private GuiButton btnMove = null;
	private GuiButton btnFlipDirection = null;
	private GuiButton btnFlip = null;
	private GuiButton btnRotateDirection = null;
	private GuiButton btnRotate = null;

	private GuiButton btnMaterials = null;
	private GuiButton btnPrint = null;

	boolean isSchematicLoaded;

	private final String strMoveSchematic = "Build schematic";//I18n.format(Names.Gui.Control.MOVE_SCHEMATIC);
	private final String strOperations = I18n.format(Names.Gui.Control.OPERATIONS);
	private final String strUnload = I18n.format(Names.Gui.Control.UNLOAD);
	private final String strMaterials = I18n.format(Names.Gui.Control.MATERIALS);
	private final String strPrinter = I18n.format(Names.Gui.Control.PRINTER);
	private final String strHide = I18n.format(Names.Gui.Control.HIDE);
	private final String strShow = I18n.format(Names.Gui.Control.SHOW);
	private final String strX = I18n.format(Names.Gui.X);
	private final String strY = I18n.format(Names.Gui.Y);
	private final String strZ = I18n.format(Names.Gui.Z);
	private final String strOn = I18n.format(Names.Gui.ON);
	private final String strOff = I18n.format(Names.Gui.OFF);

	public GuiSchematicControl(final GuiScreen guiScreen) {
		super(guiScreen);
		this.schematic = ClientProxy.schematic;
		this.printer = SchematicPrinter.INSTANCE;
	}

	@Override
	public void initGui() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;

		this.buttonList.clear();

		int id = 0;

		this.numericX = new GuiNumericField(this.fontRenderer, id++, this.centerX - 50, this.centerY - 50, 100, 20);
		this.numericX.setEnabled(false);
		this.buttonList.add(this.numericX);	

		this.numericY = new GuiNumericField(this.fontRenderer, id++, this.centerX - 50, this.centerY - 25, 100, 20);
		this.numericY.setEnabled(false);
		this.buttonList.add(this.numericY);

		this.numericZ = new GuiNumericField(this.fontRenderer, id++, this.centerX - 50, this.centerY + 0, 100, 20);
		this.numericZ.setEnabled(false);
		this.buttonList.add(this.numericZ);

		this.btnUnload = new GuiButton(id++, this.width - 90, this.height - 175, 80, 20, this.strUnload);
		this.buttonList.add(this.btnUnload);

		this.btnLayerMode = new GuiButton(id++, this.width - 90, this.height - 147, 80, 20, I18n.format((this.schematic != null ? this.schematic.layerMode : LayerMode.ALL).name));
		this.buttonList.add(this.btnLayerMode);

		this.nfLayer = new GuiNumericField(this.fontRenderer, id++, this.width - 90, this.height - 120, 80, 20);
		this.buttonList.add(this.nfLayer);

		this.btnFlipDirection = new GuiButton(id++, this.width - 180, this.height - 55, 80, 20, I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisFlip.getName()));
		this.btnFlipDirection.enabled = false;
		this.buttonList.add(this.btnFlipDirection);

		this.btnFlip = new GuiUnicodeGlyphButton(id++, this.width - 90, this.height - 55, 80, 20, " " + I18n.format(Names.Gui.Control.FLIP), "\u2194", 2.0f);
		this.buttonList.add(this.btnFlip);

		this.btnRotateDirection = new GuiButton(id++, this.width - 180, this.height - 30, 80, 20, I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisRotation.getName()));
		this.btnRotateDirection.enabled = false;
		this.buttonList.add(this.btnRotateDirection);

		this.btnRotate = new GuiUnicodeGlyphButton(id++, this.width - 90, this.height - 30, 80, 20, " " + I18n.format(Names.Gui.Control.ROTATE), "\u21bb", 2.0f);
		this.buttonList.add(this.btnRotate);

		this.btnMaterials = new GuiButton(id++, 10, this.height - 70, 80, 20, this.strMaterials);
		this.buttonList.add(this.btnMaterials);

		this.btnPrint = new GuiButton(id++, 10, this.height - 30, 80, 20, this.printer.isPrinting() ? this.strOn : this.strOff);
		this.buttonList.add(this.btnPrint);

		boolean isSchematicLoaded = ClientProxy.schematic != null
				? true : false;
		setButtons(isSchematicLoaded);

	}
	private void setButtons(boolean enableButtons) {

		this.btnLayerMode.enabled = enableButtons;
		this.nfLayer.setEnabled(enableButtons && this.schematic.layerMode != LayerMode.ALL);

		this.btnFlip.enabled = enableButtons;
		this.btnRotate.enabled = enableButtons;
		this.btnMaterials.enabled = enableButtons;
		this.btnPrint.enabled = enableButtons && this.printer.isEnabled();

		setMinMax(this.numericX);
		setMinMax(this.numericY);
		setMinMax(this.numericZ);

		if (enableButtons) {
			setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
		}

		this.nfLayer.setMinimum(0);
		this.nfLayer.setMaximum(enableButtons ? this.schematic.getHeight() - 1 : 0);
		if (enableButtons) {
			this.nfLayer.setValue(this.schematic.renderingLayer);
		}

		this.btnUnload.displayString = enableButtons ? "Unload" : "Load";
		this.btnUnload.enabled = true;
	}


	private void setMinMax(final GuiNumericField numericField) {
		numericField.setMinimum(Constants.World.MINIMUM_COORD);
		numericField.setMaximum(Constants.World.MAXIMUM_COORD);
	}

	private void setPoint(final GuiNumericField numX, final GuiNumericField numY, final GuiNumericField numZ, final BlockPos point) {
		numX.setValue(point.getX());
		numY.setValue(point.getY());
		numZ.setValue(point.getZ());
	}

	@Override
	protected void actionPerformed(final GuiButton guiButton) {
		if (guiButton.enabled) {

			if (guiButton.id == this.numericX.id) {

			} else if (guiButton.id == this.numericY.id) {

			} else if (guiButton.id == this.numericZ.id) {

			} 
			else if (guiButton.id == this.btnUnload.id) {

				if(ClientProxy.schematic != null) 
					Schemes.proxy.unloadSchematic(true);
				else 
					Schemes.proxy.loadSchematicFromController(Minecraft.getMinecraft().player);
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (guiButton.id == this.btnLayerMode.id) {
				this.schematic.layerMode = LayerMode.next(this.schematic.layerMode);
				this.btnLayerMode.displayString = I18n.format(this.schematic.layerMode.name);
				this.nfLayer.setEnabled(this.schematic.layerMode != LayerMode.ALL);
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.nfLayer.id) {
				this.schematic.renderingLayer = this.nfLayer.getValue();
				RenderSchematic.INSTANCE.refresh();
				
//			} else if (guiButton.id == this.btnHide.id) {
				//this.mc.displayGuiScreen(this.parentScreen);
				//this.btnHide.displayString = this.schematic.toggleRendering() ? this.strHide : this.strShow;
//			} else if (guiButton.id == this.btnMove.id) {
//
//			} else if (guiButton.id == this.btnFlipDirection.id) {

			} else if (guiButton.id == this.btnFlip.id) {
				if (FlipHelper.INSTANCE.flip(this.schematic, ClientProxy.axisFlip, isShiftKeyDown())) {
					
					ClientProxy.updater.updateServer(new MBlockPos(), "", 0, new MBlockPos(), 4);

					finalizeTransformation();
				}
			} else if (guiButton.id == this.btnRotateDirection.id) {

			} 
			else if (guiButton.id == this.btnRotate.id) {
				setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
				if (RotationHelper.INSTANCE.rotate(this.schematic, ClientProxy.axisRotation, isShiftKeyDown())) {
					setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
					
					ClientProxy.updater.updateServer(new MBlockPos(), "", 0, new MBlockPos(), 5);
				
					finalizeTransformation();	
				}
			} 

			else if (guiButton.id == this.btnMaterials.id) {
				this.mc.displayGuiScreen(new GuiSchematicMaterials(this));
			} else if (guiButton.id == this.btnPrint.id && this.printer.isEnabled()) {
				final boolean isPrinting = this.printer.togglePrinting();
				this.btnPrint.displayString = isPrinting ? this.strOn : this.strOff;
			}
		}

	}

	private void finalizeTransformation() {

		RenderSchematic.INSTANCE.refresh();
		SchematicPrinter.INSTANCE.refresh();

		ClientProxy.rotationRender = ClientProxy.controller.getRotationRender();
		ClientProxy.offsetSchematic(schematic);
		
		
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();

		if (this.btnFlip.enabled) {
			this.btnFlip.packedFGColour = isShiftKeyDown() ? 0xFF0000 : 0x000000;
		}

		if (this.btnRotate.enabled) {
			this.btnRotate.packedFGColour = isShiftKeyDown() ? 0xFF0000 : 0x000000;
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {

		drawCenteredString(this.fontRenderer, this.strMoveSchematic, this.centerX, this.centerY - 65, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, this.strMaterials, 50, this.height - 85, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, this.strPrinter, 50, this.height - 45, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, this.strOperations, this.width - 50, this.height - 70, 0xFFFFFF);

		drawString(this.fontRenderer, this.strX, this.centerX - 65, this.centerY - 45, 0xFFFFFF);
		drawString(this.fontRenderer, this.strY, this.centerX - 65, this.centerY - 20, 0xFFFFFF);
		drawString(this.fontRenderer, this.strZ, this.centerX - 65, this.centerY + 5, 0xFFFFFF);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
