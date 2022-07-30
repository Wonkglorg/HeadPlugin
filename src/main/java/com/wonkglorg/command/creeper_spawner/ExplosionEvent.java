package com.wonkglorg.command.creeper_spawner;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;

public class ExplosionEvent implements Listener
{
	@EventHandler
	public void explosionEvent(EntityExplodeEvent e)
	{
		if(e.getEntity() instanceof Creeper creeper)
		{
			if(creeper.getPersistentDataContainer().has(new NamespacedKey("no_damage", "non_block_removal"), PersistentDataType.INTEGER))
			{
				int result = creeper.getPersistentDataContainer().get(new NamespacedKey("no_damage", "non_block_removal"), PersistentDataType.INTEGER);
				if(result == 1)
				{
					e.blockList().clear();
				}
			}
		}
	}
}