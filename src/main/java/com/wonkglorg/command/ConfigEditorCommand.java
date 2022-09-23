package com.wonkglorg.command;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.MobHeadData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigEditorCommand extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public ConfigEditorCommand(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		MobHeadData.getAllValidConfigHeadData(Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),"Heads");
		//
		
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
	
	//Add way to edit each individual mob settings by typing /head-edit <mobname> <List of sub types if available auto fills>
	// then you get an interactive list of all the stats which are clickable and can be changed by typing in chat after the click and confirming
	

	
}