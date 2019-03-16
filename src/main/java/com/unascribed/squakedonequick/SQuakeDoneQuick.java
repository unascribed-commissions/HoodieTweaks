package com.unascribed.squakedonequick;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="squakedonequick", name="SQuake Done Quick", version="@VERSION@")
public class SQuakeDoneQuick {

	@Instance
	public static SQuakeDoneQuick inst;

	@SidedProxy(clientSide="com.unascribed.squakedonequick.ClientProxy", serverSide="com.unascribed.squakedonequick.Proxy")
	public static Proxy proxy;

	public boolean hideHealth = true;
	public boolean allDamageKills = true;
	public boolean woolPreventsFallDamage = true;
	public boolean hayPreventsFallDamage = true;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		hideHealth = cfg.getBoolean("hideHealth", "features", true, "If true, the health bar will always be hidden.");
		allDamageKills = cfg.getBoolean("allDamageKills", "features", true, "If true, all damage dealt to players will cause instant death.");
		woolPreventsFallDamage = cfg.getBoolean("woolPreventsFallDamage", "features", true, "If true, landing on wool will not deal any fall damage.");
		hayPreventsFallDamage = cfg.getBoolean("hayPreventsFallDamage", "features", true, "If true, landing on wool will not deal any fall damage.");
		cfg.save();
		proxy.onPreInit();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent e) {
		if (allDamageKills && e.getEntityLiving() instanceof EntityPlayer) {
			e.setAmount(65535);
		}
	}

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
		if (woolPreventsFallDamage) {
			e.getRegistry().register(new BlockWoolNoFall());
		}
		if (hayPreventsFallDamage) {
			e.getRegistry().register(new BlockHayNoFall());
		}
	}
}
