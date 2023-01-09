package com.wonkglorg.command.creeper_spawner;

import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargedCreeper extends Command
{
	private static final Map<Player, String> playerStringMap = new HashMap<>();
	private final List<String> stringList = new ArrayList<>();
	
	public ChargedCreeper(@NotNull JavaPlugin main, @NotNull String name)
	{
		super(main, name);
		
		stringList.add("charged");
		stringList.add("default");
		stringList.add("non");
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		String firstParam = argAsString(0);
		
		if(firstParam.equalsIgnoreCase("charged") || firstParam.equalsIgnoreCase("default") || firstParam.equalsIgnoreCase("non"))
		{
			playerStringMap.put(player, firstParam);
		}
		Message.msgPlayer(player, "Click ground to spawn creepers of type: " + firstParam);
		return true;
	}
	
	List<String> matches;
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		matches = new ArrayList<>();
		if(args.length == 1)
		{
			StringUtil.copyPartialMatches(args[0], stringList, matches);
			Collections.sort(matches);
			return matches;
		}
		
		return null;
	}
	
	public static Map<Player, String> getPlayerStringMap()
	{
		return playerStringMap;
	}
}