package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;

public class HorseProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Horse horse = (Horse) entity;
		String name = horse.getType().toString().toLowerCase();
		String type = horse.getColor().name().toLowerCase();
		return "Heads." + name + "." + type;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Horse;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity,loc);
	}
}