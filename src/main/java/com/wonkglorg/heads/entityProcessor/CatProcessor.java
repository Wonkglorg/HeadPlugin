package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;

public class CatProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Cat cat = (Cat) entity;
		String name = cat.getType().toString().toLowerCase();
		String variant = cat.getCatType().name().toLowerCase();
		return  "Heads." + name + "." + variant;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Cat;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}