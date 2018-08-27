package com.mr208.ea.client;

import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.common.CommonProxy;
import com.mr208.ea.common.ModContent;
import com.mr208.ea.common.items.ItemCluster;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event)
	{
		for(Item item : ModContent.modItems)
		{
			if(item instanceof ItemCluster)
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(ExpandedArcanum.MOD_ID,"cluster"),"inventory"));
			}
			else if(item.getRegistryName()!= null)
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
		
		for(Block block : ModContent.modBlocks)
		{
			Item item = Item.getItemFromBlock(block);
			if(block.getRegistryName()!=null && item.getRegistryName() != new ResourceLocation("minecraft","air"))
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
			}
			else
			{
				ModelLoader.setCustomStateMapper(block, new StateMapperBase()
				{
					@Override
					protected ModelResourceLocation getModelResourceLocation(IBlockState state)
					{
						return new ModelResourceLocation(block.getRegistryName().toString());
					}
				});
			}
		}
	}
	
	@SubscribeEvent
	public static void onRegisterItemColors(final ColorHandlerEvent.Item event)
	{
		final IItemColor clusterColorHandler = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				if(stack.getItem() instanceof ItemCluster)
				{
					switch(tintIndex)
					{
						case 0:
							return Color.WHITE.getRGB();
						case 1:
							return ((ItemCluster)stack.getItem()).getOverlayColor();
					}
				}
				
				return Color.BLACK.getRGB();
			}
		};
		
		for(ItemCluster cluster:ModContent.modClusters)
			event.getItemColors().registerItemColorHandler(clusterColorHandler, cluster);
	}
}
