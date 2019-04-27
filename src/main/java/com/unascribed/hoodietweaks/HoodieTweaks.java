package com.unascribed.hoodietweaks;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
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

@Mod(modid="hoodietweaks", name="Hoodie Tweaks", version="@VERSION@")
public class HoodieTweaks {

	@Instance
	public static HoodieTweaks inst;

	@SidedProxy(clientSide="com.unascribed.hoodietweaks.ClientProxy", serverSide="com.unascribed.hoodietweaks.Proxy")
	public static Proxy proxy;

	public boolean hideHealth = true;
	public boolean allDamageKills = true;
	public Set<Block> noFallDamageBlocks = Sets.newHashSet();

	public int bedrockHarvestLevel = -1;
	public float bedrockHardness = -1;
	public boolean bedrockDrops = false;
	public boolean bedrockDropsSilkTouchOnly = false;
	public List<String> bedrockTools = Lists.newArrayList();
	public boolean bedrockUnbreakableAtY0 = false;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		File oldCfgFile = new File(e.getModConfigurationDirectory(), "squakedonequick.cfg");
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		if (oldCfgFile.exists()) {
			Configuration oldCfg = new Configuration(oldCfgFile);
			oldCfg.load();
			boolean woolPreventsFallDamage = oldCfg.getBoolean("woolPreventsFallDamage", "features", true, "legacy");
			boolean hayPreventsFallDamage = oldCfg.getBoolean("hayPreventsFallDamage", "features", true, "legacy");
			String[] prefill;
			if (woolPreventsFallDamage && hayPreventsFallDamage) {
				prefill = new String[] {"minecraft:wool","minecraft:hay_block"};
			} else if (woolPreventsFallDamage) {
				prefill = new String[] {"minecraft:wool"};
			} else if (hayPreventsFallDamage) {
				prefill = new String[] {"minecraft:hay_block"};
			} else {
				prefill = new String[]{};
			}
			cfg.get("features", "noFallDamageBlocks", new String[] {}).set(prefill);
			cfg.get("features", "hideHealth", false).set(oldCfg.getBoolean("hideHealth", "features", true, ""));
			cfg.get("features", "allDamageKills", false).set(oldCfg.getBoolean("allDamageKills", "features", true, ""));
			oldCfgFile.delete();
		}
		hideHealth = cfg.getBoolean("hideHealth", "features", false, "If true, the health bar will always be hidden.");
		allDamageKills = cfg.getBoolean("allDamageKills", "features", false, "If true, all damage dealt to players will cause instant death.");
		String[] noFallBlocks = cfg.getStringList("noFallDamageBlocks", "features", new String[] {}, "A list of block IDs that will not deal any fall damage.");
		for (String s : noFallBlocks) {
			noFallDamageBlocks.add(Block.getBlockFromName(s));
		}
		bedrockHarvestLevel = cfg.getInt("harvestLevel", "bedrock", -1, -1, 1000, "Bedrock's harvest level.\n-1 means unchanged. 0 is wooden tools, 1 is stone, 2 is iron, 3 is diamond.\nFurther values are defined by other mods.\nNote: If this setting is -1, bedrock will not be overridden at all, and other settings in this section will do nothing!");
		bedrockHardness = cfg.getFloat("hardness", "bedrock", -1, -1, 65536, "Bedrock's hardness. -1 means unbreakable. 50 is obsidian.");
		String[] bedrockToolsArr = cfg.getStringList("tools", "bedrock", new String[]{"pickaxe"}, "A list of tool classes that are considered the proper tool for bedrock.");
		for (String s : bedrockToolsArr) {
			bedrockTools.add(s);
		}
		String bedrockDropsStr = cfg.getString("drops", "bedrock", "false", "Whether or not bedrock drops an item when broken with the correct tool class and harvest level.\nValid values: false, true, silk_touch");
		switch (bedrockDropsStr) {
			case "false":
				bedrockDrops = false;
				bedrockDropsSilkTouchOnly = false;
				break;
			case "true":
				bedrockDrops = true;
				bedrockDropsSilkTouchOnly = false;
				break;
			case "silk_touch":
				bedrockDrops = true;
				bedrockDropsSilkTouchOnly = true;
				break;
		}
		bedrockUnbreakableAtY0 = cfg.getBoolean("unbreakableAtY0", "bedrock", false, "If true, bedrock at y=0 cannot be broken, to prevent void holes.");
		cfg.save();
		proxy.onPreInit();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent e) {
		if (e.getSource() == DamageSource.FALL) {
			Block on = e.getEntityLiving().world.getBlockState(e.getEntityLiving().getPosition().down()).getBlock();
			if (noFallDamageBlocks.contains(on)) {
				e.setCanceled(true);
				return;
			}
		}
		if (allDamageKills && e.getEntityLiving() instanceof EntityPlayer) {
			e.setAmount(65535);
		}
	}

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
		if (bedrockHarvestLevel != -1) {
			e.getRegistry().register(new BlockNewBedrock());
		}
	}
}
