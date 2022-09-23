package com.wonkglorg.command.creeper_spawner;

import com.wonkglorg.utilitylib.listener.EventListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionEvent extends EventListener
{
	public ExplosionEvent(JavaPlugin plugin)
	{
		super(plugin);
	}
	
	@EventHandler
	public void explosionEvent(EntityExplodeEvent e)
	{
		if(e.getEntity() instanceof Creeper creeper)
		{
			if(creeper.getPersistentDataContainer().has(new NamespacedKey(plugin, "noblockdamage"), PersistentDataType.INTEGER))
			{
				int result = creeper.getPersistentDataContainer().get(new NamespacedKey(plugin, "noblockdamage"),
						PersistentDataType.INTEGER);
				if(result == 1)
				{
					e.blockList().clear();
				}
			}
		}
	}
}