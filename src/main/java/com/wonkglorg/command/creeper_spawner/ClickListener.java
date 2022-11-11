package com.wonkglorg.command.creeper_spawner;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickListener implements Listener
{
	private long delay;
	private final JavaPlugin plugin;
	
	public ClickListener(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void PlayerInteraction(PlayerInteractEvent e)
	{
		Action action = e.getAction();
		Block block = e.getClickedBlock();
		if(block == null)
		{
			return;
		}
		if(action.isRightClick() && e.getItem() == null && System.currentTimeMillis() - delay > 100 && block.isSolid())
		{
			String type = ChargedCreeper.getPlayerStringMap().get(e.getPlayer());
			if(type == null || type.equalsIgnoreCase("non"))
			{
				return;
			}
			World world = e.getPlayer().getWorld();
			Location loc = e.getInteractionPoint();
			Creeper creeper = (Creeper) world.spawnEntity(loc, EntityType.CREEPER);
			creeper.setExplosionRadius(30);
			creeper.getPersistentDataContainer().set(new NamespacedKey(plugin, "noblockdamage"), PersistentDataType.INTEGER, 1);
			if(type.equalsIgnoreCase("charged"))
			{
				creeper.setPowered(true);
			}
			creeper.explode();
			delay = System.currentTimeMillis();
		}
	}
}