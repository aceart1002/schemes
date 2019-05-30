package aceart.registry;

import com.github.lunatrius.schematica.reference.Reference;

import aceart.blocks.tiles.ContainsTile;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Registrator {


	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {

		for(Registrable unregistered : ModBlocks.BLOCKS) {
			String customName = unregistered.getCustomRegistryName();

			((Block)unregistered).setUnlocalizedName(customName);
			((Block)unregistered).setRegistryName(customName);

		}
		
		IForgeRegistry<Block> registry = event.getRegistry();
		for(Registrable unregistered : ModBlocks.BLOCKS) {
			registry.register((Block)unregistered);
		}
		
		for(ContainsTile<?> blockWithTile : ModBlocks.TILES) {
			GameRegistry.registerTileEntity((blockWithTile).getTileEntityClass(), ((Registrable)blockWithTile).getCustomRegistryName());
		}

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		for(Registrable block : ModBlocks.BLOCKS) {
			Block unregistered = (Block) block;
			Item itemBlock = new ItemBlock(unregistered).setRegistryName
					(unregistered.getRegistryName());
			registry.register(itemBlock);
		}

	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {

		for(Registrable block : ModBlocks.BLOCKS) {
			Block unregistered = (Block) block;
			Item item = Item.getItemFromBlock(unregistered);
			String id = unregistered.getRegistryName().toString();
			ModelLoader.setCustomModelResourceLocation(
					item, 0, new ModelResourceLocation
					(Reference.MODID + ":" + id, "inventory"));
		}
	}

}
