package com.wonkglorg.command.creeper_spawner;

import com.wonkglorg.utilitylib.abstraction.Command;
import com.wonkglorg.utilitylib.utils.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargedCreeper extends Command
{
	private static final Map<Player, String> playerStringMap = new HashMap<>();
	
	public ChargedCreeper(@NotNull JavaPlugin main, @NotNull String name)
	{
		super(main, name);
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		if(args[0].equalsIgnoreCase("charged") || args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("non"))
		{
			playerStringMap.put(player, args[0]);
		}
		Message.msgPlayer(player, "You now spawn creepers of type: " + args[0]);
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		if(args.length == 1)
		{
			List<String> stringList = new ArrayList<>();
			stringList.add("charged");
			stringList.add("default");
			stringList.add("non");
			return stringList;
		}
		
		return null;
	}
	
	public static Map<Player, String> getPlayerStringMap()
	{
		return playerStringMap;
	}
}