package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
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
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeathListener implements Listener
{
	List<EntityTypeProcessor> entityTypeProcessors = new ArrayList<>();
	Config config = Heads.getManager().getConfigManager().getConfig(YML.CONFIG.getFileName());
	
	private final JavaPlugin plugin;
	
	public DeathListener(JavaPlugin plugin)
	{
		this.plugin = plugin;
		addProcessors(entityTypeProcessors);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e)
	{
		Entity mob = e.getEntity();
		if(Objects.equals(mob.getPersistentDataContainer().get(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING), "true"))
		{
			if(mob instanceof Player player)
			{
				mob.getWorld().dropItemNaturally(mob.getLocation(), ItemUtility.createPlayerHead(player.getUniqueId()));
				mob.getPersistentDataContainer().set(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING, "false");
			}
			for(EntityTypeProcessor processor : entityTypeProcessors)
			{
				if(processor.matches(mob))
				{
					//Check how to detect why a creeper exploded? for negating some people from getting heads and to log
					removeDefaultHeadDrops(e.getDrops(), e.getEntity().getType());
					processor.process(mob, e.getEntity().getLocation());
					return;
				}
			}
			return;
		}
		
	}
	
	private void removeDefaultHeadDrops(List<ItemStack> drops, EntityType entity)
	{
		if(entity == EntityType.CREEPER && config.getBoolean("Remove_Vanilla_Creeper_Head"))
		{
			drops.remove(new ItemStack(Material.CREEPER_HEAD));
			return;
		}
		if(entity == EntityType.SKELETON && config.getBoolean("Remove_Vanilla_Skeleton_Head"))
		{
			drops.remove(new ItemStack(Material.SKELETON_SKULL));
			return;
		}
		if(entity == EntityType.ZOMBIE && config.getBoolean("Remove_Vanilla_Zombie_Head"))
		{
			drops.remove(new ItemStack(Material.ZOMBIE_HEAD));
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