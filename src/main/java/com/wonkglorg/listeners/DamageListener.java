package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.listener.EventListener;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageListener extends EventListener
{
	private final Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.CONFIG.getFileName());
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	
	public DamageListener(JavaPlugin plugin)
	{
		super(plugin);
	}
	
	@EventHandler
	public void onCreeperDamage(EntityDamageByEntityEvent e)
	{
		if(config.getBoolean("Player_Kill_Entity_Head"))
		{
			processEntityDeath(e);
		}
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
			if(player.getHealth() > e.getFinalDamage())
			{
				return;
			}
			if(Math.random() * 100 <= config.getInt("Player_PvP_Head_DropChance"))
			{
				ItemStack playerHead = ItemUtility.createPlayerHead(player.getUniqueId());
				player.getWorld().dropItemNaturally(player.getLocation(),
						ItemUtility.addLore(playerHead, lang.getValue(player, "pvp-head-description").replace("<killer>", damager.getName())));
			}
		}
		
	}
	
	private void processEntityDeath(EntityDamageByEntityEvent e)
	{
		
		//ADD VALUES FROM THIS METHOD TO CONFIG FILE!!!!!!
		if(e.getDamager() instanceof Player player && e.getEntity() instanceof LivingEntity livingEntity)
		{
			if(livingEntity instanceof Player){
				return;
			}
			if(livingEntity.getHealth() > e.getFinalDamage())
			{
				return;
			}
			double bonus = 0;
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if(itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_MOBS))
			{
				bonus = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
			}
			
			if(Math.random() * 100 <=
			   config.getInt("Player_Kill_Entity_Dropchance") + (bonus * config.getDouble("Player_kill_entity_looting_modifier")))
			{
				livingEntity.getPersistentDataContainer().set(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING, "true");
			}
			
		}
	}
	
	private void processCreeperDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Creeper creeper && e.getEntity() instanceof LivingEntity livingEntity)
		{
			if(config.getBoolean("Charged_Creeper_Required"))
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
			livingEntity.getPersistentDataContainer().set(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING, "true");
		}
	}
}

