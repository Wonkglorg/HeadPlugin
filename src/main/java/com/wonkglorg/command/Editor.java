package com.wonkglorg.command;

import com.wonkglorg.utilitylib.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Editor extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public Editor(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	//add funcionality to edit config using webpage,
	
	//store session and add command to apply changes ingame similar to luckyperms

	
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