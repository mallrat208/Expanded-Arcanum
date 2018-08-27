package com.mr208.ea.common;

import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.common.items.ItemBase;
import com.mr208.ea.common.items.ItemCluster;
import com.mr208.ea.common.items.ItemMimicFork;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;

@EventBusSubscriber
public class ModContent
{
	public static ArrayList<Item> modItems = new ArrayList<>();
	public static ArrayList<ItemCluster> modClusters = new ArrayList<>();
	public static ArrayList<Block> modBlocks = new ArrayList<>();
	
	public static ItemBase itemPocketNoteblock;
	
	public static void onPreInit()
	{
		itemPocketNoteblock = new ItemMimicFork();
	}
	
	public static void onInit()
	{
		for(ItemCluster cluster:modClusters)
			cluster.registerOreTag();
			
		OreDictionary.registerOre("blockVoid", new ItemStack(BlocksTC.metalBlockVoid));
	
	}
	
	public static void onPostInit()
	{
		for(ItemCluster cluster:modClusters)
			cluster.registerCluster();
			
		ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ExpandedArcanum.MOD_ID, "mimic_fork"),
				new ShapedArcaneRecipe(new ResourceLocation(""),"EA_MIMICFORK", 15, new AspectList().add(Aspect.AIR,5), new ItemStack(ModContent.itemPocketNoteblock), "S S", " M ", "SRS", 'S', "slabWood", 'M', ItemsTC.mechanismSimple, 'R', Items.REDSTONE));
	}
	
	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> event)
	{
		for(Item item:modItems)
			event.getRegistry().register(item);
		
		for(ItemCluster cluster:modClusters)
			cluster.registerSmeltingRecipes();
	}
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event)
	{
		for(Block block:modBlocks)
			event.getRegistry().register(block);
	}
}
