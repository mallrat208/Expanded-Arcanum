package com.mr208.ea.modules.immersiveengineering;

import blusunrize.immersiveengineering.api.ApiUtils;
import com.mr208.ea.ExpandedArcanum;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventsIE
{
	public ClientEventsIE()
	{
	
	}
	
	public static ClientEventsIE INSTANCE = new ClientEventsIE();
	
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		for(ItemDrillheadEA head: ModuleIE.drills)
		{
			head.drillStat.sprite = ApiUtils.getRegisterSprite(event.getMap(), head.drillStat.texture);
		}
	}
}
