package com.wonkglorg.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;

public class LlamaProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Llama llama = (Llama) entity;
		String name = llama.getName().toLowerCase();
		String type = llama.getColor().name().toLowerCase();
		return "Heads." + name + "." + type;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Llama;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity,loc);
	}
}