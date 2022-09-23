package com.wonkglorg.command.headgive;

import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.utils.Utils;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
	public boolean execute(@NotNull Player player, String[] args)
	{
		String texture;
		String name;
		String description;
		if(args.length < 1)
		{
			return false;
		}
		if(args.length == 1)
		{
			texture = args[0];
			Utils.give(player,ItemUtility.createCustomHead(texture, "&rCustom Head"));
			return true;
		}
		if(args.length == 2)
		{
			texture = args[0];
			name = args[1];
			Utils.give(player,ItemUtility.createCustomHead(texture, name));
			return true;
		}
		
		texture = args[0];
		name = args[1];
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 2; args.length > i; i++)
		{
			stringBuilder.append(args[i]);
		}
		description = stringBuilder.toString();
		
		//Add multi line support
		Utils.give(player,ItemUtility.createCustomHead(texture, name, description));
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
}