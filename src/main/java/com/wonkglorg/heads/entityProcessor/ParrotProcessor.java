package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;

public class ParrotProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Parrot parrot = (Parrot) entity;
		String name = parrot.getType().toString().toLowerCase();
		String type = parrot.getVariant().name().toLowerCase();
		return "Heads." + name + "." + type;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Parrot;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}