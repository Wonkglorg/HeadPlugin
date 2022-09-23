package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.English;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.listener.EventListener;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.Message;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class Milk extends EventListener
{
	
	private final JavaPlugin plugin;
	private final Config config = Heads.getPluginManager().getConfigManager().getConfig("config.yml");
	private final int drinkCooldown = config.getInt("milk-drink-delay");
	private final int milkingCooldown = config.getInt("milking-recharge-delay");
	
	public Milk(JavaPlugin plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void MilkFriend(PlayerInteractEvent e)
	{
		if(!e.getPlayer().hasPermission("Heads.Milk.Player"))
		{
			return;
		}
		if(!e.getAction().isRightClick())
		{
			return;
		}
		ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();
		if(itemStack.getType() == Material.BUCKET && e.getPlayer().getTargetEntity(5) instanceof LivingEntity target)
		{
			Player player = e.getPlayer();
			PersistentDataContainer dataContainer = target.getPersistentDataContainer();
			NamespacedKey namespacedKey = new NamespacedKey(plugin, "lactation");
			if(dataContainer.has(namespacedKey))
			{
				long lastMilking = dataContainer.get(namespacedKey, PersistentDataType.LONG);
				
				long secondsLeft = ((lastMilking / 1000) + milkingCooldown) - (System.currentTimeMillis() / 1000);
				if(secondsLeft > 0)
				{
					Message.msgPlayer(player,
							English.MILK_FAIL_OBTAIN.toString()
													.replace("<target>", target.getName())
													.replace("<seconds>", String.valueOf(secondsLeft)));
					return;
				}
			}
			dataContainer.set(namespacedKey, PersistentDataType.LONG, System.currentTimeMillis());
			ItemUtility.removeItems(player, 1, itemStack.clone());
			player.getInventory().addItem(new ItemBuilder(Material.MILK_BUCKET).setName(English.MILK_ITEM_NAME.toString().replace("<target>", target.getName()))
																			   .addLoreLine(English.MILK_ITEM_DESCRIPTION.toString().replace("<target>",target.getName()))
																			   .glow()
																			   .build());
			player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 100, 1);
			if(target instanceof Player target1)
			{
				Message.msgPlayer(target1, English.MILK_SUCCESS_OBTAIN_MILKED.toString().replace("<player>", player.getName()));
			}
			Message.msgPlayer(player, English.MILK_SUCCESS_OBTAIN_MILKER.toString().replace("<target>", target.getName()));
			
		}
	}
	
	@EventHandler
	public void milkDrink(PlayerItemConsumeEvent e)
	{
		if(e.getItem().containsEnchantment(Enchantment.LUCK) && e.getItem().getType() == Material.MILK_BUCKET)
		{
			Player player = e.getPlayer();
			PersistentDataContainer dataContainer = player.getPersistentDataContainer();
			NamespacedKey namespacedKey = new NamespacedKey(plugin, "lactation");
			if(dataContainer.has(namespacedKey))
			{
				long lastMilking = dataContainer.get(namespacedKey, PersistentDataType.LONG);
				long secondsLeft = ((lastMilking / 1000) + drinkCooldown) - (System.currentTimeMillis() / 1000);
				if(secondsLeft > 0)
				{
					Message.msgPlayer(player, English.MILK_DRINK_FAIL.toString().replace("<player>", player.getName()));
					e.setCancelled(true);
					return;
				}
			}
			dataContainer.set(namespacedKey, PersistentDataType.LONG, System.currentTimeMillis());
			
			player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
			Message.msgPlayer(player, English.MILK_DRINK_SUCCESS.toString().replace("<player>", player.getName()));
		}
	}
	
	//Milk player
}