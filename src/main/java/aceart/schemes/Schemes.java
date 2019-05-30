package aceart.schemes;

import java.util.Map;

import com.github.lunatrius.schematica.proxy.CommonProxy;
import com.github.lunatrius.schematica.reference.Reference;

import aceart.network.ServerMessageProcessor;
import aceart.network.UpdateMessage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class Schemes {
	
    @Mod.Instance(Reference.MODID)
    public static Schemes instance;

    @SidedProxy(serverSide = Reference.PROXY_SERVER, clientSide = Reference.PROXY_CLIENT)
    public static CommonProxy proxy;

    public static final SchemesTab SCHEMES = new SchemesTab();
    
    @NetworkCheckHandler
    public boolean checkModList(final Map<String, String> versions, final Side side) {
        return true;
    }
	
    public static SimpleNetworkWrapper wrapper;
	private static final byte MESSAGE_ID = 64;
	
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);
                wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID + Reference.VERSION);
        wrapper.registerMessage(ServerMessageProcessor.class, UpdateMessage.class, 
				MESSAGE_ID, Side.SERVER);
        
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
        
//        NetworkRegistry.INSTANCE.registerGuiHandler(Schematica.instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
    
    
  
}
