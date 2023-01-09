package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Entity;

public class AxolotlProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Axolotl axolotl = (Axolotl) entity;
		String name = axolotl.getType().toString().toLowerCase();
		String variant = axolotl.getVariant().name().toLowerCase();
		return  "Heads." + name + "." + variant;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Axolotl;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity,loc);
	}
}