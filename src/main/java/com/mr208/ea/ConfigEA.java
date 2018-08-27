package com.mr208.ea;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = ExpandedArcanum.MOD_ID,name = "ExpandedArcanum")
public class ConfigEA
{
	@Comment("Immersive Engineering Compatibility Module. Enables Thaumic Drill Heads and registers Aspects")
	public static IEModule IE_MODULE = new IEModule();
	
	@Comment("Chococraft Compatibility Module. Enables recipes for Ability Fruit and registers Aspects")
	public static ChococraftModule CHOCOCRAFT_MODULE = new ChococraftModule();
	
	public static class IEModule
	{
		@Comment("Enables the IE Module")
		public boolean enableodule = true;
	}
	
	public static class ChococraftModule
	{
		@Comment("Enables the Chococraft Module")
		public boolean enableModule = true;
	}
}
