package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;

public class SheepProcessor extends EntityTypeProcessor
{
	
	@Override
	String path()
	{
		Sheep sheep = (Sheep) entity;
		String name = sheep.getType().toString().toLowerCase();
		String color = sheep.getColor().name().toLowerCase();
		return "Heads." + name + "." + color;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Sheep;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
	
}