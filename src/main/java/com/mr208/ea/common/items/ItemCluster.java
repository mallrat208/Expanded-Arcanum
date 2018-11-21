package com.mr208.ea.common.items;

import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.common.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.utils.Utils;

import java.util.Iterator;

public class ItemCluster extends ItemBase
{
	private ItemStack nuggetStack;
	private ItemStack ingotStack;
	private String metalName;
	private String oreName;
	private String clusterName;
	private String researchKey;
	private float chanceEarths;
	private int overlayColor;
	
	
	public ItemCluster(String name,String research,ItemStack ingot, ItemStack nugget, float earthsChance, int overlay)
	{
		super("cluster_"+name.toLowerCase());
		this.ingotStack = ingot;
		this.nuggetStack = nugget;
		this.metalName = name;
		this.oreName = "ore"+name;
		this.clusterName = "cluster"+name;
		this.chanceEarths = earthsChance;
		this.overlayColor = overlay;
		this.researchKey = research;
		
		ModContent.modClusters.add(this);
	}
	
	public int getOverlayColor()
	{
		return overlayColor;
	}
	
	public void registerSmeltingRecipes()
	{
		GameRegistry.addSmelting(this, ingotStack, 0.7F);
	}
	
	public void registerOreTag()
	{
		OreDictionary.registerOre(clusterName, new ItemStack(this,1));
	}
	
	public void registerCluster()
	{
		ThaumcraftApi.addSmeltingBonus(oreName, nuggetStack.copy());
		ThaumcraftApi.addSmeltingBonus(this, nuggetStack.copy());
		ThaumcraftApi.addSmeltingBonus(oreName, new ItemStack(ItemsTC.nuggets,1,10), chanceEarths);
		
		ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ExpandedArcanum.MOD_ID, "metal_purification_"+metalName.toLowerCase()),
				new CrucibleRecipe("EA_METALPURIFICATION_IE", new ItemStack(this,1), oreName, new AspectList().add(Aspect.METAL,5).add(Aspect.ORDER,5)));
		
		
		if(OreDictionary.doesOreNameExist(oreName))
		{
			Iterator itr;
			ItemStack stack;
			
			for(itr = OreDictionary.getOres(oreName, false).iterator(); itr.hasNext();)
			{
				stack =(ItemStack)itr.next();
				Utils.addSpecialMiningResult(stack, new ItemStack(this), 1.0F);
			}
		}
	}
}
