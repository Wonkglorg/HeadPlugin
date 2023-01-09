package com.wonkglorg.heads.entityProcessor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ZombieVillager;

public class ZombieVillagerProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		ZombieVillager zombieVillager = (ZombieVillager) entity;
		String type = zombieVillager.getVillagerType().name().toLowerCase();
		String profession = zombieVillager.getVillagerProfession().name().toLowerCase();
		return "Heads.zombie villager." + type + "." + profession;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof ZombieVillager;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		processHeadData(entity, loc);
	}
}