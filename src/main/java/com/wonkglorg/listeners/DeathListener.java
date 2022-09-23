package com.wonkglorg.listeners;

import com.wonkglorg.entityProcessor.AxolotlProcessor;
import com.wonkglorg.entityProcessor.CatProcessor;
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
import com.wonkglorg.utilitylib.listener.EventListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeathListener extends EventListener
{
	List<EntityTypeProcessor> entityTypeProcessors = new ArrayList<>();
	
	public DeathListener(JavaPlugin plugin)
	{
		super(plugin);
		addProcessors(entityTypeProcessors);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e)
	{
		Entity mob = e.getEntity();
		if(Objects.equals(mob.getPersistentDataContainer().get(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING), "true")){
			for(EntityTypeProcessor processor : entityTypeProcessors)
			{
				if(processor.matches(mob))
				{
					processor.process(mob, e.getEntity().getLocation());
					return;
				}
			}
		}
		
	}
	
	private void addProcessors(List<EntityTypeProcessor> entityTypeProcessors)
	{
		entityTypeProcessors.add(new AxolotlProcessor());
		entityTypeProcessors.add(new CatProcessor());
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