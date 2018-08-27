package com.mr208.ea.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Instrument;
import net.minecraftforge.event.world.NoteBlockEvent.Note;
import net.minecraftforge.event.world.NoteBlockEvent.Octave;
import net.minecraftforge.event.world.NoteBlockEvent.Play;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.tiles.devices.TileArcaneEar;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMimicFork extends ItemBase
{
	public ItemMimicFork()
	{
		super("mimic_fork");
		this.setMaxStackSize(1);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setByte("note", (byte)1);
		tagCompound.setInteger("tone", 1);
		
		stack.setTagCompound(tagCompound);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(stack.hasTagCompound() && stack.getTagCompound()!=null)
		{
			NBTTagCompound tagCompound = stack.getTagCompound();
			
			if(tagCompound.hasKey("tone"))
				tooltip.add(I18n.format("ea.mimic_fork.instrument",I18n.format( "ea.mimic_fork.instrument." + getInstrumentNameFromInt(tagCompound.getInteger("tone")))));
			if(tagCompound.hasKey("note"))
			{
				tooltip.add(I18n.format("ea.mimic_fork.note",tagCompound.getByte("note")));
			}
		}
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack heldStack = player.getHeldItem(hand);
		NBTTagCompound tagCompound = heldStack.getTagCompound();
		
		if(tagCompound==null)
		{
			tagCompound=new NBTTagCompound();
			heldStack.setTagCompound(tagCompound);
		}
		
		TileEntity tile;
		tile=world.getTileEntity(pos);
		
		if(!world.isRemote && player.isSneaking())
		{

			if(tile!=null&&tile instanceof TileEntityNote)
			{
				IBlockState iblockstate=world.getBlockState(pos.down());
				
				tagCompound.setByte("note", ((TileEntityNote)tile).note);
				tagCompound.setInteger("tone", getToneFromState(iblockstate));
				
				return EnumActionResult.SUCCESS;
			} else if(tile!=null && tile instanceof TileArcaneEar)
			{
				tagCompound.setByte("note", ((TileArcaneEar)tile).note);
				tagCompound.setInteger("tone", ((TileArcaneEar)tile).tone);
			}
		}else if(!world.isRemote && !player.isSneaking())
			{
				if(tile!=null && tile instanceof TileArcaneEar)
				{
					((TileArcaneEar)tile).note = tagCompound.getByte("note");
					tile.markDirty();
					return EnumActionResult.SUCCESS;
				}
				
				if(tile!=null && tile instanceof TileEntityNote)
				{
					((TileEntityNote)tile).note = tagCompound.getByte("note");
					tile.markDirty();
					return EnumActionResult.SUCCESS;
				}
			}
		
		
		return EnumActionResult.PASS;
	}
	
	private int getToneFromState(IBlockState iblockstate)
	{
		Material material = iblockstate.getMaterial();
		int i = 0;
		
		if (material == Material.ROCK)
		{
			i = 1;
		}
		
		if (material == Material.SAND)
		{
			i = 2;
		}
		
		if (material == Material.GLASS)
		{
			i = 3;
		}
		
		if (material == Material.WOOD)
		{
			i = 4;
		}
		
		Block block = iblockstate.getBlock();
		
		if (block == Blocks.CLAY)
		{
			i = 5;
		}
		
		if (block == Blocks.GOLD_BLOCK)
		{
			i = 6;
		}
		
		if (block == Blocks.WOOL)
		{
			i = 7;
		}
		
		if (block == Blocks.PACKED_ICE)
		{
			i = 8;
		}
		
		if (block == Blocks.BONE_BLOCK)
		{
			i = 9;
		}
		
		return i;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 100;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound tagCompound = stack.getTagCompound();
			
			int note = 0;
			int tone = 0;
			
			note = tagCompound.getByte("note");
			tone = tagCompound.getInteger("tone");
			
			NoteBlockEvent.Play event = new NoteBlockEvent.Play(worldIn, entityLiving.getPosition(), null, note, tone);
			
			if(!MinecraftForge.EVENT_BUS.post(event))
			{
				float f = (float) Math.pow(2.0D, (note -12)/12.0D);
				
				
				worldIn.playSound(null, entityLiving.getPosition().up(1),getInstrumentSound(event.getInstrument()), SoundCategory.PLAYERS, 3.0F, f);
				worldIn.spawnParticle(EnumParticleTypes.NOTE, entityLiving.posX + 0.5D, entityLiving.posY + 0.5D, entityLiving.posZ + 0.5D, (double) note/24.0D, 0.0D, 0.0D);
			}
			
		}
	}
	
	private String getInstrumentNameFromInt(int toneIn)
	{
		return Instrument.values()[toneIn].name().toLowerCase();
	}
	
	private SoundEvent getInstrumentSound(Instrument instrumentIn)
	{
		switch(instrumentIn)
		{
			case BELL:
				return SoundEvents.BLOCK_NOTE_BELL;
			case CHIME:
				return SoundEvents.BLOCK_NOTE_CHIME;
			case FLUTE:
				return SoundEvents.BLOCK_NOTE_FLUTE;
			case PIANO:
				return SoundEvents.BLOCK_NOTE_HARP;
			case SNARE:
				return SoundEvents.BLOCK_NOTE_SNARE;
			case CLICKS:
				return SoundEvents.BLOCK_NOTE_HAT;
			case GUITAR:
				return SoundEvents.BLOCK_NOTE_GUITAR;
			case BASSDRUM:
				return SoundEvents.BLOCK_NOTE_BASEDRUM;
			case XYLOPHONE:
				return SoundEvents.BLOCK_NOTE_XYLOPHONE;
			default:
				return SoundEvents.BLOCK_NOTE_BASS;
		}
	}
}
