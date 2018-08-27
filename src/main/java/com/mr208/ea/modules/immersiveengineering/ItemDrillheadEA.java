package com.mr208.ea.modules.immersiveengineering;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IDrillHead;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mr208.ea.common.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDrillheadEA extends ItemBase implements IDrillHead
{
	final String HEAD_DAMAGE = "headDamage";
	public DrillStats drillStat;
	
	public ItemDrillheadEA(String materialName, DrillStats drillStat)
	{
		super("drillhead_"+materialName);
		
		this.setMaxStackSize(1);
		this.drillStat = drillStat;
		
		ModuleIE.drills.add(this);
	}
	
	public DrillStats getStats(ItemStack head)
	{
		if(head.getItem() instanceof ItemDrillheadEA)
			return ((ItemDrillheadEA)head.getItem()).drillStat;
		
		return new DrillStats("",0,0,0,0,0,0,"immersiveengineering:items/drill_diesel");
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return Utils.compareToOreName(repair,getStats(toRepair).repairMaterial);
	}
	
	@Override
	public void addInformation(ItemStack head, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.size", getStats(head).drillSize, getStats(head).drillDepth));
		tooltip.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.level", Utils.getHarvestLevelName(getMiningLevel(head))));
		tooltip.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.speed", Utils.formatDouble(getMiningSpeed(head), "0.###")));
		tooltip.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.damage", Utils.formatDouble(getAttackDamage(head), "0.###")));
		
		int maxDmg = getMaximumHeadDamage(head);
		int dmg = maxDmg-getHeadDamage(head);
		float quote = dmg/(float)maxDmg;
		String status = ""+(quote < .1?TextFormatting.RED: quote < .3?TextFormatting.GOLD: quote < .6?TextFormatting.YELLOW: TextFormatting.GREEN);
		String s = status+(getMaximumHeadDamage(head)-getHeadDamage(head))+"/"+getMaximumHeadDamage(head);
		tooltip.add(I18n.format(Lib.DESC_INFO+"durability", s));
	}
	
	@Override
	public boolean beforeBlockbreak(ItemStack drill, ItemStack head, EntityPlayer entityPlayer)
	{
		return false;
	}
	
	@Override
	public void afterBlockbreak(ItemStack drill, ItemStack head, EntityPlayer entityPlayer)
	{
	
	}
	
	@Override
	public int getMiningLevel(ItemStack head)
	{
		return getStats(head).drillLevel;
	}
	
	@Override
	public float getMiningSpeed(ItemStack head)
	{
		return getStats(head).drillSpeed;
	}
	
	@Override
	public float getAttackDamage(ItemStack head)
	{
		return getStats(head).drillAttack;
	}
	
	@Override
	public int getHeadDamage(ItemStack head)
	{
		return ItemNBTHelper.getInt(head, HEAD_DAMAGE);
	}
	
	@Override
	public int getMaximumHeadDamage(ItemStack head)
	{
		return getStats(head).maxDamage;
	}
	
	@Override
	public void damageHead(ItemStack head, int i)
	{
		ItemNBTHelper.setInt(head,HEAD_DAMAGE, ItemNBTHelper.getInt(head, HEAD_DAMAGE)+i);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getDrillTexture(ItemStack drill, ItemStack head)
	{
		return getStats(head).sprite;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack head)
	{
		return (double)ItemNBTHelper.getInt(head,HEAD_DAMAGE)/getMaximumHeadDamage(head);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack head)
	{
		return ItemNBTHelper.getInt(head, HEAD_DAMAGE) > 0;
	}
	
	@Override
	public ImmutableList<BlockPos> getExtraBlocksDug(ItemStack head, World world, EntityPlayer player, RayTraceResult mop)
	{
		EnumFacing side = mop.sideHit;
		int diameter = getStats(head).drillSize;
		int depth = getStats(head).drillDepth;
		
		BlockPos startPos = mop.getBlockPos();
		IBlockState state = world.getBlockState(startPos);
		Block block = state.getBlock();
		float maxHardness = 1;
		if(block!=null&&!block.isAir(state, world, startPos))
			maxHardness = state.getPlayerRelativeBlockHardness(player, world, startPos)*0.6F;
		if(maxHardness < 0)
			maxHardness = 0;
		
		if(diameter%2==0)//even numbers
		{
			float hx = (float)mop.hitVec.x-mop.getBlockPos().getX();
			float hy = (float)mop.hitVec.y-mop.getBlockPos().getY();
			float hz = (float)mop.hitVec.z-mop.getBlockPos().getZ();
			if((side.getAxis()==Axis.Y&&hx < .5)||(side.getAxis()==Axis.Z&&hx < .5))
				startPos = startPos.add(-diameter/2, 0, 0);
			if(side.getAxis()!=Axis.Y&&hy < .5)
				startPos = startPos.add(0, -diameter/2, 0);
			if((side.getAxis()==Axis.Y&&hz < .5)||(side.getAxis()==Axis.X&&hz < .5))
				startPos = startPos.add(0, 0, -diameter/2);
		}
		else//odd numbers
		{
			switch(side.getAxis())
			{
				case X:
					startPos = startPos.add(0, -1, -diameter/2);
					break;
				case Y:
					startPos = startPos.add(-diameter/2, 0, -diameter/2);
					break;
				case Z:
					startPos = startPos.add(-diameter/2, -1, 0);
					break;
			}
			
			//startPos = startPos.add(-(side.getAxis()==Axis.X?0:diameter/2), -(side.getAxis()==Axis.Y?0: diameter/2), -(side.getAxis()==Axis.Z?0: diameter/2));
		}
		
		Builder<BlockPos> b = ImmutableList.builder();
		for(int dd = 0; dd < depth; dd++)
			for(int dw = 0; dw < diameter; dw++)
				for(int dh = 0; dh < diameter; dh++)
				{
					BlockPos pos = startPos.add((side.getAxis()==Axis.X?dd: dw), (side.getAxis()==Axis.Y?dd: dh), (side.getAxis()==Axis.Y?dh: side.getAxis()==Axis.X?dw: dd));
					if(pos.equals(mop.getBlockPos()))
						continue;
					state = world.getBlockState(pos);
					block = state.getBlock();
					float h = state.getPlayerRelativeBlockHardness(player, world, pos);
					boolean canHarvest = block.canHarvestBlock(world, pos, player);
					boolean drillMat = ((ItemDrill)IEContent.itemDrill).isEffective(state.getMaterial());
					boolean hardness = h > maxHardness;
					if(canHarvest&&drillMat&&hardness)
						b.add(pos);
				}
		return b.build();
	}
	
	public static class DrillStats
	{
		final String repairMaterial;
		final int drillSize;
		final int drillDepth;
		final int drillLevel;
		final float drillSpeed;
		final float drillAttack;
		final int maxDamage;
		public final String texture;
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite sprite;
		
		public DrillStats(String repairMaterial, int drillSize, int drillDepth, int drillLevel, float drillSpeed, int drillAttack, int maxDamage, String texture)
		{
			this.repairMaterial = repairMaterial;
			this.drillSize = drillSize;
			this.drillDepth = drillDepth;
			this.drillLevel = drillLevel;
			this.drillSpeed = drillSpeed;
			this.drillAttack = drillAttack;
			this.maxDamage = maxDamage;
			this.texture = texture;
		}
	}
}
