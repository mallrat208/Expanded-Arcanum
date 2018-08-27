package com.mr208.ea.modules.immersiveengineering;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import com.google.common.collect.ImmutableSet;
import com.mr208.ea.common.items.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class ItemUpgrade extends ItemBase implements IUpgrade
{
	public UpgradeStat upgradeStat;
	
	public ItemUpgrade(UpgradeStat upgradeStat)
	{
		super(upgradeStat.name);
		this.upgradeStat = upgradeStat;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		String[] flavorText =ImmersiveEngineering.proxy.splitStringOnWidth(I18n.format("item.ea."+upgradeStat.name+".tooltip"),200);
		for(String line:flavorText)
			tooltip.add(line);
	}
	
	@Override
	public Set<String> getUpgradeTypes(ItemStack itemStack)
	{
		return upgradeStat.toolset;
	}
	
	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		BiPredicate<ItemStack, ItemStack> check = upgradeStat.applyCheck;
		if(check!=null&&target.getItem() instanceof ItemUpgrade)
			return check.test(target, upgrade);
		return true;
	}
	
	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		upgradeStat.function.accept(upgrade, modifications);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return upgradeStat.stackSize;
	}
	
	public static class UpgradeStat
	{
		private String name;
		private ImmutableSet<String> toolset;
		private int stackSize = 1;
		private BiPredicate<ItemStack, ItemStack> applyCheck;
		private BiConsumer<ItemStack, NBTTagCompound> function;
		
		UpgradeStat(String name, ImmutableSet<String> toolset, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(name, toolset, 1, function);
		}
		
		UpgradeStat(String name, ImmutableSet<String> toolset, int stackSize, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(name, toolset, stackSize, null, function);
		}
		
		public UpgradeStat(String name, ImmutableSet<String> toolset, int stackSize, BiPredicate<ItemStack, ItemStack> check, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this.name = name;
			this.toolset = toolset;
			this.stackSize = stackSize;
			this.applyCheck = check;
			this.function = function;
		}
	}
}
