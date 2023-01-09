package com.wonkglorg.listeners.advancements;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.enums.PersistentContainer;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementListener implements Listener
{
	private final AdvancementHandler manager;
	private final JavaPlugin plugin;
	
	public AdvancementListener(JavaPlugin plugin, AdvancementHandler manager)
	{
		this.manager = manager;
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e)
	{
		if(!(e.getEntity() instanceof Player player))
		{
			return;
		}
		if(e.getItem().getItemStack().getType() != Material.PLAYER_HEAD)
		{
			return;
		}
		ItemStack itemStack = e.getItem().getItemStack();
		for(AdvancementData advancementData : manager.getAdvancements().values())
		{
			Advancement advancement = advancementData.getAdvancement();
			
			if(!advancement.isGranted(player))
			{
				continue;
			}
			
			String criteria = itemStack.getItemMeta().getPersistentDataContainer().get(PersistentContainer.PERSISTENT_ADVANCEMENT.getNamespaceKey(),
					PersistentDataType.STRING);
			
			if(advancementData.hasCriteria(null))
			{
				manager.getManager().grantCriteria(player, advancement, criteria);
				manager.getManager().saveProgress(player, advancement);
			}
			//check for specific value on a skull and what it is null check if the value exists and nullcheck if the head exists
		}
		//find way to dynamically get the things needed and check if they got picked up
	}
	
}