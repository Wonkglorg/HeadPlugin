package com.wonkglorg.listeners.advancements;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener
{
	private final AdvancementManager manager;
	private final JavaPlugin plugin;
	
	public JoinListener(JavaPlugin plugin, AdvancementManager manager)
	{
		this.plugin = plugin;
		this.manager = manager;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				manager.loadProgress(e.getPlayer());
				manager.addPlayer(e.getPlayer());
			}
		}.runTaskLater(plugin, 4);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		manager.saveProgress(e.getPlayer());
		manager.removePlayer(e.getPlayer());
	}
	
}