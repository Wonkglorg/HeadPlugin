package com.wonkglorg.heads.entityProcessor;

import org.bukkit.entity.EntityType;

public enum EntityProcessorEnums
{
	Axolotl(new AxolotlProcessor(), EntityType.AXOLOTL),
	CAT(new CatProcessor(), EntityType.CAT),
	HORSE(new HorseProcessor(), EntityType.HORSE),
	LLAMA(new LlamaProcessor(), EntityType.LLAMA),
	PARROT(new ParrotProcessor(), EntityType.PARROT),
	PLAYER(new PlayerProcessor(), EntityType.PLAYER),
	RABBIT(new RabbitProcessor(), EntityType.RABBIT),
	SHEEP(new SheepProcessor(), EntityType.SHEEP),
	TRADER_LLAMA(new TraderLlamaProcessor(), EntityType.TRADER_LLAMA),
	VILLAGER(new VillagerProcessor(), EntityType.VILLAGER),
	ZOMBIE_VILLAGER(new ZombieVillagerProcessor(), EntityType.ZOMBIE_VILLAGER),
	
	;
	
	private final EntityTypeProcessor entityTypeProcessor;
	private final EntityType type;
	
	EntityProcessorEnums(EntityTypeProcessor entityTypeProcessor, EntityType type)
	{
		this.entityTypeProcessor = entityTypeProcessor;
		this.type = type;
	}
	
	public EntityType type()
	{
		return type;
	}
	
	public EntityTypeProcessor processor()
	{
		return entityTypeProcessor;
	}
}