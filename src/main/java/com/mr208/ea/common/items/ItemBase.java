package com.mr208.ea.common.items;

import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.common.ModContent;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBase extends Item
{
	EnumRarity rarity;
	
	public ItemBase(String name)
	{
		this(name, EnumRarity.COMMON);
	}
	
	public ItemBase(String name, EnumRarity rarity)
	{
		this.setUnlocalizedName(ExpandedArcanum.MOD_ID + "." + name);
		this.setRegistryName(new ResourceLocation(ExpandedArcanum.MOD_ID, name));
		this.setCreativeTab(ExpandedArcanum.CREATIVE_TAB);
		this.rarity = rarity;
		
		ModContent.modItems.add(this);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBase)
			return ((ItemBase)stack.getItem()).rarity;
		
		return super.getRarity(stack);
	}
}
