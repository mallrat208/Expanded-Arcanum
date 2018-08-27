package com.mr208.ea;

import com.mr208.ea.ExpandedArcanum;
import com.mr208.ea.modules.EAModule;
import com.mr208.ea.modules.IEAModule;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleManager
{
	public static final List<IEAModule> MODULES = new LinkedList<>();
	
	static void onPreInit(FMLPreInitializationEvent event)
	{
		Set<ASMData> asmDataSet = event.getAsmData().getAll(EAModule.class.getName());
		
		ExpandedArcanum.LOGGER.info("Found {} modules.", asmDataSet.size());
		
		for(ASMDataTable.ASMData asmData : asmDataSet)
		{
			Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
			String dependency = (String) annotationInfo.get("dependency");
			
			boolean success = true;
			
			if(dependency == null || dependency.isEmpty())
			{
				ExpandedArcanum.LOGGER.error("No dependency specified for addon: {}", asmData.getClassName());
			} else {
				if(Loader.isModLoaded(dependency))
				{
					try
					{
						Class<?> clazz = Class.forName(asmData.getClassName());
						
						if(IEAModule.class.isAssignableFrom(clazz))
						{
							IEAModule module = (IEAModule) clazz.newInstance();
							MODULES.add(module);
						}
					}
					catch(ClassNotFoundException e)
					{
						ExpandedArcanum.LOGGER.error("Failed to load class for module: {}", dependency);
						success = false;
					}
					catch(InstantiationException e)
					{
						ExpandedArcanum.LOGGER.error("Module: {}, couldn't be instantiated", dependency);
						success = false;
					}
					catch(IllegalAccessException e)
					{
						ExpandedArcanum.LOGGER.error("Could not access constructor for module {}", dependency);
						success = false;
					}
					
					if(success)
					{
						ExpandedArcanum.LOGGER.info("Loaded Module {}", dependency);
					}
					else
					{
						ExpandedArcanum.LOGGER.info("Failed to load Module {}", dependency);
					}
				}
			}
		}
	}
}
