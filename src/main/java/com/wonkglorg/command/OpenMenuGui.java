package com.wonkglorg.command;

import com.wonkglorg.Heads;
import com.wonkglorg.gui.HeadMenuGui;
import com.wonkglorg.utilitylib.manager.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpenMenuGui extends Command
{
	private final Heads plugin;
	
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public OpenMenuGui(@NotNull Heads plugin, @NotNull String name)
	{
		super(plugin, name);
		this.plugin = plugin;
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(Player player, String[] args)
	{
		new HeadMenuGui(plugin, Heads.getProfileManager().get(player)).open();
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
}