package com.mr208.ea.modules.immersiveengineering;

import blusunrize.immersiveengineering.api.tool.IDrillHead;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import com.mr208.ea.ExpandedArcanum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.common.lib.utils.Utils;

public class EventsIE
{
	public EventsIE() {}
	
	static EventsIE INSTANCE = new EventsIE();
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer) || event.getEntity().world.isRemote)
			return;
		
		EntityPlayer player = (EntityPlayer)event.getEntity();
		
		if(player.ticksExisted % 20 == 0)
		{
			if(!player.getHeldItemMainhand().isEmpty()&&player.getHeldItemMainhand().getItem() instanceof ItemDrill)
				updateDrill(player, EnumHand.MAIN_HAND);
			
			if(!player.getHeldItemOffhand().isEmpty()&&player.getHeldItemOffhand().getItem() instanceof ItemDrill)
				updateDrill(player, EnumHand.OFF_HAND);
		}
	}
	
	@SubscribeEvent
	public void onHarvestDropsEvent(HarvestDropsEvent event)
	{
		if(!event.getWorld().isRemote && event.getHarvester() != null)
		{
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(heldItem.getItem() instanceof ItemDrill)
			{
				int refining = ((ItemDrill)IEContent.itemDrill).getUpgrades(heldItem).getInteger("refining");
				if(refining > 0)
				{
					refining++;
					
					float chance = (float)refining * 0.125f;
					boolean success = false;
					for(int x = 0; x < event.getDrops().size(); x++)
					{
						ItemStack dropStack = event.getDrops().get(x);
						ItemStack specialDrops = Utils.findSpecialMiningResult(dropStack, chance, event.getWorld().rand);
						
						if(!dropStack.isItemEqual(specialDrops))
						{
							event.getDrops().set(x, specialDrops);
							success = true;
						}
					}
					
					if(success)
					{
						event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.7F + event.getWorld().rand.nextFloat() * 0.2F);
					}
				}
				
			}
		}
	}
	
	@SubscribeEvent
	public void onRecipeRegistryEvent(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation("thaumcraft:thaumium_stuff"),ModuleIE.driillHeadThaumium, "II ","BBI","II ", 'I', "ingotThaumium", 'B', "blockThaumium").setRegistryName("drill_thaumium"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation("thaumcraft:void_stuff"),ModuleIE.drillHeadVoid, "II ","BBI","II ", 'I', "ingotVoid", 'B', "blockVoid").setRegistryName("drill_void"));
	}
	
	void updateDrill(EntityPlayer player, EnumHand hand)
	{
		ItemStack drillStack = player.getHeldItem(hand);
		
		ItemStack headStack = ((ItemDrill)drillStack.getItem()).getHead(drillStack);
		
		if(!headStack.isEmpty() && headStack.getItem() == ModuleIE.drillHeadVoid && ((IDrillHead)headStack.getItem()).getHeadDamage(headStack) > 0)
		{
			((IDrillHead)headStack.getItem()).damageHead(headStack, -1);
		}
	}
}
