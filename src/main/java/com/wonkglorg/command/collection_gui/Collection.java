package com.wonkglorg.command.collection_gui;

import com.wonkglorg.utilitylib.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Collection extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public Collection(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		return false;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
}