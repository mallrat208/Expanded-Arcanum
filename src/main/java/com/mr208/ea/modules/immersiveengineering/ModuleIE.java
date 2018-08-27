package com.mr208.ea.modules.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableSet;
import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.common.items.ItemCluster;
import com.mr208.ea.modules.EAModule;
import com.mr208.ea.modules.IEAModule;
import com.mr208.ea.modules.immersiveengineering.ItemDrillheadEA.DrillStats;
import com.mr208.ea.modules.immersiveengineering.ItemUpgrade.UpgradeStat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;

@EAModule(dependency ="immersiveengineering")
public class ModuleIE implements IEAModule
{
	protected static ArrayList<ItemDrillheadEA> drills = new ArrayList<>();
	
	static ItemDrillheadEA  driillHeadThaumium;
	static ItemDrillheadEA drillHeadVoid;
	
	static ItemUpgrade upgradeRefining;
	
	static ItemCluster clusterNickel;
	static ItemCluster clusterBauxite;
	static ItemCluster clusterUranium;
	
	@Override
	public void onPreInit()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			MinecraftForge.EVENT_BUS.register(ClientEventsIE.INSTANCE);
		
		MinecraftForge.EVENT_BUS.register(EventsIE.INSTANCE);
		
		clusterBauxite = new ItemCluster("Aluminum","EA_METAL_PURIFICATION_IE", new ItemStack(IEContent.itemMetal, 2,1), new ItemStack(IEContent.itemMetal, 1, 21), 0.01F, 0xbcbcbc);
		clusterNickel = new ItemCluster("Nickel","EA_METAL_PURIFICATION_IE", new ItemStack(IEContent.itemMetal, 2,4), new ItemStack(IEContent.itemMetal, 1, 24), 0.01F, 0xe9efdc);
		clusterUranium = new ItemCluster("Uranium","EA_METAL_PURIFICATION_IE", new ItemStack(IEContent.itemMetal, 2,5), new ItemStack(IEContent.itemMetal, 1, 25), 0.025F, 0x79896b);
		
		upgradeRefining = new ItemUpgrade(new UpgradeStat("upgrade_refining",
				ImmutableSet.of("DRILL"),4,
				(upgrade, modifications) -> ItemNBTHelper.modifyInt(modifications, "refining", upgrade.getCount())));
		
		DrillStats stats = new DrillStats("ingotThaumium",3,1,2,12,6,6000,"ea:items/drill_thaumium");
		driillHeadThaumium = new ItemDrillheadEA("thaumium",stats);
		
		stats = new DrillStats("ingotVoid",5, 1,3, 8, 7, 8000, "ea:items/drill_void");
		drillHeadVoid = new ItemDrillheadEA("void", stats);
	}
	
	@Override
	public void onInit()
	{
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(ExpandedArcanum.MOD_ID,"research/ie_module"));
	}
	
	@Override
	public void onPostInit()
	{
		ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ExpandedArcanum.MOD_ID,"upgrade_refining"), new InfusionRecipe("EA_IEUPGREFINING", new ItemStack(upgradeRefining), 1,(new AspectList().add(Aspect.ORDER, 80).add(Aspect.EXCHANGE, 60)),new ItemStack(ItemsTC.mechanismSimple), new ItemStack(ItemsTC.salisMundus),new ItemStack(ItemsTC.nuggets,1,10), new ItemStack(ItemsTC.salisMundus), new ItemStack(ItemsTC.nuggets,1,10)));
		
	}
}
