package aceart.registry;

import aceart.blocks.BlockSchemeController;
import aceart.blocks.BlockSchemeSaver;
import aceart.blocks.tiles.ContainsTile;
import blocks.BlockRailsBuilder;
import net.minecraft.block.material.Material;


public class ModBlocks {
	
	public static final Registrable SAVER = new BlockSchemeSaver(Material.ROCK, "scheme_saver");
	public static final Registrable CONTROLLER = new BlockSchemeController(Material.ROCK, "scheme_controller");
	
	public static final Registrable RAILS = new BlockRailsBuilder(Material.ROCK, "scheme_rails_builder");
	
	public static final ContainsTile SCHEME_CONTROL_TILE = (ContainsTile) CONTROLLER;
	public static final ContainsTile SCHEME_SAVER_TILE = (ContainsTile) SAVER;
	
	public static final Registrable[] BLOCKS = {SAVER, CONTROLLER, RAILS};
	public static final ContainsTile[] TILES = {SCHEME_CONTROL_TILE, SCHEME_SAVER_TILE};
}
