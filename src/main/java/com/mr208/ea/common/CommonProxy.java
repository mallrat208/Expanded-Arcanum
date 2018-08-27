package com.mr208.ea.common;

import com.mr208.ea.ExpandedArcanum;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEvent.Research;

public class CommonProxy
{
	public void onPreInit()
	{
		ModContent.onPreInit();
	}
	
	public void onInit()
	{
		ModContent.onInit();
		
		ResearchCategories.registerCategory("EXPANDEDARCANUM", "FIRSTSTEPS",
				new AspectList(),
				new ResourceLocation("thaumcraft", "textures/items/mechanism_simple.png"),
				new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_2.jpg"),
				new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_over.png"));
		
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(ExpandedArcanum.MOD_ID, "research/expandedarcanum"));
		
	}
	
	public void onPostInit()
	{
		ModContent.onPostInit();
		
		initResearch();
	}
	
	
	protected void initResearch()
	{
	}
}
