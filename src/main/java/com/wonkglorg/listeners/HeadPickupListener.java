package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.listener.EventListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadPickupListener extends EventListener
{
	public HeadPickupListener(JavaPlugin plugin)
	{
		super(plugin);
	}
	
	@EventHandler
	public void itemPickup(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player){
			if(e.getItem().getPersistentDataContainer().get(new NamespacedKey(Heads.getInstance(),"newdrop"), PersistentDataType.STRING) != null){
				//add that head to the collection and other stuff
			}
		
		}
	}
}