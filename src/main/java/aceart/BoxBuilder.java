package aceart;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BoxBuilder {
	char mC;
	char sC;
	char lC;
	
	BlockPos centralBlock;
	int radius = 5;

	int mainDir;
	
	private ArrayList<BlockPos> box = new ArrayList<BlockPos>();

	public BoxBuilder(BlockPos centralBlock, int maxDistanceFromCenter, EnumFacing playerFacing, EnumFacing blockSide) {

		this.centralBlock = centralBlock;
		this.radius = maxDistanceFromCenter;
		setInitialConditions(playerFacing, blockSide);
		
	}

	void setInitialConditions(EnumFacing playerFacing, EnumFacing blockSide) {
		
		switch(playerFacing) {
		case EAST: 	mC = 'x'; sC = 'z'; lC = 'y';
					mainDir = 1;  
					break;
		case WEST: 	mC = 'x'; sC = 'z'; lC = 'y';
					mainDir = -1; 
					break;
		case SOUTH: mC = 'z'; sC = 'x'; lC = 'y';
					mainDir = 1; 
					break;
		case NORTH: mC = 'z'; sC = 'x'; lC = 'y'; 
					mainDir = -1; 
					break;
		case UP: 	
			switch(blockSide) {
			case EAST:
			case WEST: mC = 'y'; sC = 'z'; lC = 'x'; mainDir = 1; break;
			case UP: 
			case DOWN:
			case SOUTH: 
			case NORTH: mC = 'y'; sC = 'x'; lC = 'z'; mainDir = 1; break;
			default: 
			} 
			break;
		case DOWN: 	
			switch(blockSide) {
			case EAST:
			case WEST: mC = 'y'; sC = 'z'; lC = 'x'; mainDir = -1; break;
			case UP: 
			case DOWN:
			case SOUTH: 
			case NORTH: mC = 'y'; sC = 'x'; lC = 'z'; mainDir = -1; break;
			} 
			break;
		}
		
	}

	public void buildBox() {
	
		int x = this.centralBlock.getX();
		int y = this.centralBlock.getY();
		int z = this.centralBlock.getZ();
		
		BlockPos centralBlock = new BlockPos(x,y,z);
		
		box.addAll(buildLayer(centralBlock));
		for(int distanceFromCenter = 1; distanceFromCenter < radius; distanceFromCenter++) {
			
			int levelCor = -distanceFromCenter;
			
			for(int i = 1; i <= 2; levelCor = -levelCor) {
				
				BlockPos nextCentral = incrementCoordinate(centralBlock, lC, levelCor);
				box.addAll(buildLayer(nextCentral));
				i++;
			}
				
		}

		box.addAll(buildLastLayer());

	}

	ArrayList<BlockPos> buildLayer(BlockPos startingCentral) {
		
		ArrayList<BlockPos> layer = new ArrayList<BlockPos>();
		
		layer.addAll(buildLine(startingCentral));
		for(int distanceFromCenter = 1; distanceFromCenter < radius; distanceFromCenter++) { 
			
			ArrayList<BlockPos> sideLines = new ArrayList<BlockPos>();
			
			int secCor = -distanceFromCenter;			
			
			for(int i = 1; i <= 2; secCor = -secCor) {
				
				BlockPos nextCentral = incrementCoordinate(startingCentral, sC, secCor);
				sideLines.addAll(buildLine(nextCentral));
				i++;
			}
			
			Collections.shuffle(sideLines);
			layer.addAll(sideLines);
		}
		return layer;
	}
	
	ArrayList<BlockPos> buildLine(BlockPos startingBlock) {
		
		ArrayList<BlockPos> line = new ArrayList<BlockPos>();
		
		line.add(startingBlock);
		for(int mainCor = 1; mainCor < radius; mainCor++) {
			BlockPos nextBlock = incrementCoordinate(startingBlock, mC, mainCor*mainDir);
			line.add(nextBlock);
			
			//TODO remove later
//			counter();
		}
		return line;	
		
	}
	
	ArrayList<BlockPos> buildLastLayer() {
		
		ArrayList<BlockPos> lastLayer = new ArrayList<>();
		BlockPos initialBlock = incrementCoordinate(centralBlock, mC, -mainDir);
		
		lastLayer.add(initialBlock);
		
		for(int loop = 1; loop < radius; loop++) {
			ArrayList<BlockPos> sides = buildSides(initialBlock, loop);
			Collections.shuffle(sides);
			
			lastLayer.addAll(sides);
		}
		
		return lastLayer;
		
	}
	
	ArrayList<BlockPos> buildSides(BlockPos initialBlock, int loop) {
		
		ArrayList<BlockPos> sides = new ArrayList<>();
		
		BlockPos nextBlock = incrementCoordinate(initialBlock, lC, -loop);
		nextBlock = incrementCoordinate(nextBlock, sC, -loop);
		
		char curCor = lC;
		int curDir = 1, nextDir = 1;
		
		for(int i = 0; i <= 4; i++) {
			
			BlockPos firstBlock = nextBlock;
			
			for(int j = 0; j <= loop*2; j++) {
				
				nextBlock = incrementCoordinate(firstBlock, curCor, j*curDir);
				sides.add(nextBlock);
			}
			
			Object[] next = nextSidePars(curCor, curDir, nextDir);
			curCor = (char) next[0]; curDir = (int) next[1]; nextDir = (int) next[2];
			
		}
		
		return sides;
	}
	
	Object[] nextSidePars(char curCor, int curDir, int nextDir) {
		int temp = curDir;
		curDir = nextDir;
		nextDir = -temp;

		//switch current coordinate
		if (curCor == lC)
			curCor = sC;
		else if (curCor == sC)
			curCor = lC;
		
		return new Object[] {curCor, curDir, nextDir};
	}
	

	public ArrayList<BlockPos> getBox() {
		return box;
	}
	
	BlockPos incrementCoordinate(BlockPos block, char whichCor, int dValue) {
		
		int dx = 0,dy = 0, dz = 0;
		switch(whichCor) {
		case 'x': dx = dValue; break;
		case 'y': dy = dValue; break;
		case 'z': dz = dValue; break;
		}
		
		int x = block.getX() + dx;
		int y = block.getY() + dy;
		int z = block.getZ() + dz;
		
		return new BlockPos(x,y,z);
		
	}

}
