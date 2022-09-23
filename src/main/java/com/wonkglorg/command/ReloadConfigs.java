package com.wonkglorg.command;

import com.wonkglorg.enums.English;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.ConfigManager;
import com.wonkglorg.utilitylib.utils.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReloadConfigs extends Command
{
	private final ConfigManager manager;
	private final Map<String, Config> configMap = new HashMap<>();
	private final List<String> configList = new ArrayList<>();
	private final List<String> matches = new ArrayList<>();
	
	public ReloadConfigs(@NotNull JavaPlugin main, @NotNull String name, ConfigManager manager)
	{
		super(main, name);
		this.manager = manager;
		initComponents();
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		if(args.length == 0)
		{
			return false;
		}
		String ymlName = args[0];
		if(ymlName.equalsIgnoreCase("ALL"))
		{
			manager.load();
			Message.msgPlayer(player, English.RELOAD_CONFIG_ALL_SUCCESS.toString());
			return true;
		}
		Config config = configMap.get(ymlName);
		
		if(config == null)
		{
			Message.msgPlayer(player, English.RELOAD_CONFIG_ERROR.toString().replace("<config>", ymlName));
			return true;
		}
		
		configMap.get(ymlName).loadConfig();
		Message.msgPlayer(player, English.RELOAD_CONFIG_SUCCESS.toString().replace("<config>", ymlName));
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		if(args.length == 1)
		{
			StringUtil.copyPartialMatches(args[0], configList, matches);
			Collections.sort(matches);
			return matches;
		}
		return null;
	}
	
	private void initComponents()
	{
		configList.add("All");
		for(YML configs : YML.values())
		{
			configList.add(configs.getFileName());
			configMap.put(configs.getFileName(), manager.getConfig(configs.getFileName()));
		}
	}
}