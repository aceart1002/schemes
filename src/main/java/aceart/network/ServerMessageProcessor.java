package aceart.network;

import aceart.schemes.Schemes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerMessageProcessor implements IMessageHandler<UpdateMessage, IMessage>{

	@Override
	public IMessage onMessage(UpdateMessage message, MessageContext ctx) {

		EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;

		final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
		playerWorldServer.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message);
				
			}
		});

		return null;
	}
	
	public void processMessage(UpdateMessage message) {
		
		int op = message.operation;
		
		if(op == 1)
			Schemes.proxy.saver.setSavedPoint(message.getPoint());
		else if(op == 2) {
			Schemes.proxy.controller.switchMode();
			Schemes.proxy.controller.setSchemeName(message.schemeName);
			Schemes.proxy.controller.setRotationRender(message.rotationRender);
			Schemes.proxy.controller.setDisplacement(message.getDisplacement());
			//Schemes.proxy.controller.setSchematic(message.schemeTag);
		}
		else if (op == 3) {
			//Schemes.proxy.controller.setSchematic(message.schemeTag);
		}
		else if(op == 4) {
			Schemes.proxy.controller.switchFlip();
		}
		else if (op == 5) {
			Schemes.proxy.controller.switchRotations();
		}
	}
	
}
