package com.wonkglorg.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TraderLlama;

public class TraderLlamaProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		TraderLlama llama = (TraderLlama) entity;
		String name = llama.getType().toString().toLowerCase();
		String type = llama.getColor().name().toLowerCase();
		return "Heads." + name + "." + type;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof TraderLlama;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity,loc);
	}
}