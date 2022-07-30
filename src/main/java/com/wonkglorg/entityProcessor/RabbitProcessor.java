package com.wonkglorg.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Rabbit;

public class RabbitProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Rabbit rabbit = (Rabbit) entity;
		String name = rabbit.getName().toLowerCase();
		String type = rabbit.getRabbitType().name().toLowerCase();
		return "Heads." + name + "." + type;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Rabbit;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}