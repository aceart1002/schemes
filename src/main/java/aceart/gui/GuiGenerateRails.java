package aceart.gui;


import java.io.IOException;
import java.util.List;

import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import railwaytoheaven.Operator;

public class GuiGenerateRails extends GuiScreen {
	
	protected final GuiScreen parentScreen;

	BlockPos pointA, pointB;
	
	GuiTextField X1Field;
	GuiTextField Y1Field;
	GuiTextField Z1Field;
	
	GuiTextField X2Field;
	GuiTextField Y2Field;
	GuiTextField Z2Field;

	GuiTextField fileNameField;
	
	GuiButton initiateButton;
	GuiButton exitButton;

	protected List<GuiTextField> fieldList = Lists.<GuiTextField>newArrayList();
	
	 public GuiGenerateRails(final GuiScreen guiScreen) {
		 this.parentScreen = guiScreen;
	    }

	    @Override
	    public void initGui() {

	        int id = 0;

	        BlockPos startPos = ClientProxy.controller.getPosition();
	        
	        X1Field = new GuiTextField(id++, fontRenderer, 10, 10, 70, 20);
	        X1Field.setText("" + startPos.getX());
	        fieldList.add(X1Field);
	        
	        Y1Field = new GuiTextField(id++, fontRenderer, 10, 30, 70, 20);
	        Y1Field.setText(""+startPos.getY());
	        fieldList.add(Y1Field);
	        
	        Z1Field = new GuiTextField(id++, fontRenderer, 10, 50, 70, 20);
	        Z1Field.setText(""+startPos.getZ());
	        fieldList.add(Z1Field);
	        
	        X2Field = new GuiTextField(id++, fontRenderer, 200, 10, 70, 20);
	        fieldList.add(X2Field);
	        
	        Y2Field = new GuiTextField(id++, fontRenderer, 200, 30, 70, 20);
	        fieldList.add(Y2Field);
	        
	        Z2Field = new GuiTextField(id++, fontRenderer, 200, 50, 70, 20);
	        fieldList.add(Z2Field);
	        
	        fileNameField = new GuiTextField(id++, fontRenderer, 50, height - 100, 150, 20);
	        fieldList.add(fileNameField);
	        
	      
	        exitButton = new GuiButton(id++, width - 320, height - 30, "Exit");
	        exitButton.setWidth(140);
	        buttonList.add(exitButton);
	        
	        initiateButton = new GuiButton(id++, width - 150, height - 30, "Start build");
	        initiateButton.setWidth(140);
	        buttonList.add(initiateButton);
	        
	        

	    }
	    
	    private void makePoints() {
	    	
	    	int x1 = Integer.parseInt(X1Field.getText());
	    	int y1 = Integer.parseInt(Y1Field.getText());
	    	int z1 = Integer.parseInt(Z1Field.getText());
	    	
	    	pointA = new BlockPos(x1, y1, z1);
	    	
	    	int x2 = Integer.parseInt(X2Field.getText());
	    	int y2 = Integer.parseInt(Y2Field.getText());
	    	int z2 = Integer.parseInt(Z2Field.getText());
	    	
	    	pointB = new BlockPos(x2, y2, z2);
	    }
	    
	    @Override
	    protected void actionPerformed(final GuiButton guiButton) {
	        if (guiButton.enabled) {
	            if(guiButton.id == initiateButton.id) {
	            	makePoints();
	            	Operator.operate(pointA, pointB, fileNameField.getText());
	            }
	            
	            if(guiButton.id == exitButton.id) {
	            	
	            }
	        }
	    }
	    @Override
	    public void drawScreen(final int x, final int y, final float partialTicks) {
	    	super.drawScreen(x, y, partialTicks);
	    	
	    	for(GuiTextField field : fieldList)
	    		field.drawTextBox();
	        
	    }
	    
	    @Override
	    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseEvent) throws IOException {
	        
	    	for(GuiTextField field : fieldList)
	    		field.mouseClicked(mouseX, mouseY, mouseEvent);

	        super.mouseClicked(mouseX, mouseY, mouseEvent);
	    }
	    
	    @Override
	    protected void keyTyped(final char character, final int code) throws IOException {
	       
	    	for(GuiTextField field : fieldList)
	    			field.textboxKeyTyped(character, code);

	        super.keyTyped(character, code);
	    }
	    
	  
}
