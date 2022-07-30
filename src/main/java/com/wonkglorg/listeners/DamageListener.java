package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.English;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.config.ConfigManager;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener
{
	private final Config config = Heads.getManager().getConfig(YML.CONFIG.getFileName());
	private ConfigManager langManager;
	
	public DamageListener()
	{
	}
	
	@EventHandler
	public void onCreeperDamage(EntityDamageByEntityEvent e)
	{
		if(config.getBoolean("Creeper_Explosion_Head"))
		{
			processCreeperDamage(e);
		}
		if(config.getBoolean("Player_PvP_Head"))
		{
			processPlayerDamage(e);
		}
		
	}
	
	private void processPlayerDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player player)
		{
			if(!e.getEntity().isDead())
			{
				return;
			}
			if(Math.random() * 100 <= config.getInt("Player_PvP_Head_DropChance"))
			{
				ItemStack playerHead = ItemUtility.createPlayerHead(player.getUniqueId());
				player.getWorld().dropItemNaturally(player.getLocation(),
						ItemUtility.addLore(playerHead, English.PVP_HEAD_DESCRIPTION.toString().replace("<killer>", damager.getName())));
			}
		}
		
	}
	
	private void processCreeperDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Creeper creeper)
		{
			if(config.getBoolean("ChargedCreeperRequired"))
			{
				if(!creeper.isPowered())
				{
					return;
				}
			}
			if(e.getEntity().getUniqueId() == creeper.getUniqueId())
			{
				return;
			}
			Heads.setArray(e.getEntity());
		}
	}
}

