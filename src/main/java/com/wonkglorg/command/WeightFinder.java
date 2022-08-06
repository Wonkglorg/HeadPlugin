package com.wonkglorg.command;

import com.wonkglorg.utilitylib.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeightFinder extends Command
{
	public WeightFinder(@NotNull JavaPlugin main, @NotNull String name)
	{
		super(main, name);
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