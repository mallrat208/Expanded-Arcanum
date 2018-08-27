package com.mr208.ea;

import com.mr208.ea.common.CommonProxy;
import com.mr208.ea.modules.IEAModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(	modid = ExpandedArcanum.MOD_ID,
		name = ExpandedArcanum.MOD_NAME,
		version = ExpandedArcanum.MOD_VERS,
		dependencies = ExpandedArcanum.MOD_DEPS)
public class ExpandedArcanum
{
	public static final String MOD_ID = "ea";
	public static final String MOD_NAME = "Expanded Arcanum";
	public static final String MOD_VERS = "@VERSION@";
	public static final String MOD_DEPS = "required-after:thaumcraft;after:immersiveengineering";
	
	private static final String PROXY_COMMON = "com.mr208.ea.common.CommonProxy";
	private static final String PROXY_CLIENT = "com.mr208.ea.client.ClientProxy";
	
	public static Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_COMMON, modId = MOD_ID)
	public static CommonProxy PROXY;
	
	@Instance(MOD_ID)
	public static ExpandedArcanum INSTANCE;
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		ModuleManager.onPreInit(event);
		
		PROXY.onPreInit();
		
		for(IEAModule module: ModuleManager.MODULES)
			module.onPreInit();
		
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		PROXY.onInit();
		
		for(IEAModule module: ModuleManager.MODULES)
			module.onInit();
		
	}
	
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		PROXY.onPostInit();
		
		for(IEAModule module: ModuleManager.MODULES)
			module.onPostInit();
		
	}
	
	public static CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(Items.BOOK);
		}
	};
	
	public Logger getLogger()
	{
		return LOGGER;
	}
}
