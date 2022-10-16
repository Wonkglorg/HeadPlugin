package com.wonkglorg.command.headgive;

import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GiveCustomHead extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public GiveCustomHead(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		
		if(args.length < 1)
		{
			return false;
		}
		if(argAsPlayer(0) != null)
		{
			return processCommand(argAsPlayer(0), 1);
		}
		return processCommand(player, 0);
	}
	
	private boolean processCommand(Player target, int offset)
	{
		StringBuilder builder = new StringBuilder();
		for(int i = 2 + offset; args.length > i; i++)
		{
			builder.append(argAsString(i)).append(" ");
		}
		ItemUtility.give(target, ItemUtility.createCustomHead(argAsString(offset), argAsString(1 + offset), builder.toString()));
		return true;
	}
	
	List<String> empty = new ArrayList<>();
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		if(args.length == 1)
		{
			return null;
		}
		return empty;
	}
}