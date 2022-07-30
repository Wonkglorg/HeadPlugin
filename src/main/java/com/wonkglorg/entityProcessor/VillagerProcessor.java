package com.wonkglorg.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

public class VillagerProcessor extends EntityTypeProcessor
{
	
	@Override
	String path()
	{
		Villager villager = (Villager) entity;
		String type = villager.getVillagerType().name().toLowerCase();
		String profession = villager.getProfession().name().toLowerCase();
		return "Heads.villager." + type + "." + profession;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Villager;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}