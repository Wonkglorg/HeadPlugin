package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		String name = entity.getType().toString().toLowerCase();
		return "Heads." + name;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof LivingEntity;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}