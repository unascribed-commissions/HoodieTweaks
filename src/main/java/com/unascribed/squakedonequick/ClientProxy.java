package com.unascribed.squakedonequick;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends Proxy {

	@Override
	public void onPreInit() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onRenderHealth(RenderGameOverlayEvent.Pre e) {
		if (SQuakeDoneQuick.inst.hideHealth && e.getType() == ElementType.HEALTH) {
			e.setCanceled(true);
		}
	}

}
