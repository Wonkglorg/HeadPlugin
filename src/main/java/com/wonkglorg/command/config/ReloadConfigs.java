package com.wonkglorg.command.config;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.logger.Logger;
import com.wonkglorg.utilitylib.managers.ConfigManager;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReloadConfigs extends Command
{
	private final ConfigManager manager;
	private final Map<String, Config> configMap = new HashMap<>();
	private final List<String> configList = new ArrayList<>();
	private final List<String> matches = new ArrayList<>();
	private final LangManager lang = Heads.getManager().getLangManager();
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
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
		String ymlName = argAsString(0);
		if(ymlName.equalsIgnoreCase("ALL"))
		{
			manager.load();
			Logger.log(lang.getValue(Locale.ENGLISH, "reload-config-all-success"));
			Message.msgPlayer(player, lang.getValue(player, "reload-config-all-success"));
			return true;
		}
		if(ymlName.equalsIgnoreCase("ALL Lang"))
		{
			lang.load();
			Message.msgPlayer(player, lang.getValue(player, "reload-config-all-lang-success"));
			return true;
		}
		Config config = configMap.get(ymlName);
		
		if(config == null)
		{
			Message.msgPlayer(player, lang.getValue(player, "reload-config-error").replace("<config>", ymlName));
			return true;
		}
		
		Message.msgPlayer(player, lang.getValue(player, "reload-config-success").replace("<config>", ymlName));
		config.load();
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
		for(YML configs : YML.values())
		{
			configList.add("ALL");
			configList.add("ALL Lang");
			configList.add(configs.getFileName());
			configMap.put(configs.getFileName(), manager.getConfig(configs.getFileName()));
		}
		for(Config lang : lang.getAllLangs().values())
		{
			configList.add(lang.name());
			configMap.put(lang.name(), lang);
		}
	}
}