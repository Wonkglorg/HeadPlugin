package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.entityProcessor.AxolotlProcessor;
import com.wonkglorg.entityProcessor.EntityProcessor;
import com.wonkglorg.entityProcessor.EntityTypeProcessor;
import com.wonkglorg.entityProcessor.HorseProcessor;
import com.wonkglorg.entityProcessor.LlamaProcessor;
import com.wonkglorg.entityProcessor.ParrotProcessor;
import com.wonkglorg.entityProcessor.PlayerProcessor;
import com.wonkglorg.entityProcessor.RabbitProcessor;
import com.wonkglorg.entityProcessor.SheepProcessor;
import com.wonkglorg.entityProcessor.TraderLlamaProcessor;
import com.wonkglorg.entityProcessor.VillagerProcessor;
import com.wonkglorg.entityProcessor.ZombieVillagerProcessor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener
{
	List<EntityTypeProcessor> entityTypeProcessors = new ArrayList<>();
	
	public DeathListener()
	{
		addProcessors(entityTypeProcessors);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e)
	{
		Entity mob = e.getEntity();
		for(Entity entity : Heads.getArray())
		{
			if(!entity.equals(mob))
			{
				continue;
			}
			entityTypeProcessors.forEach(entityTypeProcessor ->
			{
				if(entityTypeProcessor.matches(mob))
				{
					entityTypeProcessor.process(mob, e.getEntity().getLocation());
				}
			});
		}
		
		Heads.getArray().removeIf(entity1 -> !entity1.isValid());
		
	}
	
	private void addProcessors(List<EntityTypeProcessor> entityTypeProcessors)
	{
		entityTypeProcessors.add(new AxolotlProcessor());
		entityTypeProcessors.add(new ParrotProcessor());
		entityTypeProcessors.add(new RabbitProcessor());
		entityTypeProcessors.add(new SheepProcessor());
		entityTypeProcessors.add(new VillagerProcessor());
		entityTypeProcessors.add(new ZombieVillagerProcessor());
		entityTypeProcessors.add(new PlayerProcessor());
		entityTypeProcessors.add(new HorseProcessor());
		entityTypeProcessors.add(new LlamaProcessor());
		entityTypeProcessors.add(new TraderLlamaProcessor());
		entityTypeProcessors.add(new EntityProcessor());
		
	}
}