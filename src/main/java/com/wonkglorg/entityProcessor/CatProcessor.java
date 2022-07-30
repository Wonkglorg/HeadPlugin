package com.wonkglorg.entityProcessor;

import com.wonkglorg.utilitylib.utils.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;

public class CatProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		Cat cat = (Cat) entity;
		String name = cat.getName().toLowerCase();
		String variant = cat.getType().name().toLowerCase();
		Bukkit.broadcast(Message.color("Name:" + name+ "Variant + " + variant));
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