package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChangeValueCommand extends Command
{
	
	private static final Map<Player, MobHeadData> playerDataChange = new HashMap<>();
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	private final Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA_BACKUP.getFileName());
	private HeadMenuUtility menuUtility;
	
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public ChangeValueCommand(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(Player player, String[] args)
	{
		
		if(!playerDataChange.containsKey(player))
		{
			Message.msgPlayer(player, lang.getValue(player, "command-value-error-change-not-set"));
			return true;
		}
		if(args.length == 0)
		{
			Message.msgPlayer(player, lang.getValue(player, "command-value-error-null-input"));
			return true;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("quit"))
		{
			playerDataChange.remove(player);
			return true;
		}
		
		playerDataChange.keySet().removeIf(Predicate.not(Player::isValid));
		
		MobHeadData mobHeadData = playerDataChange.get(player);
		HeadMenuUtility menuUtility = HeadMenuUtility.get(player);
		this.menuUtility = menuUtility;
		StringBuilder builder = new StringBuilder();
		
		if(menuUtility == null || menuUtility.getDataVariable() == null)
		{
			Message.msgPlayer(player, lang.getValue(player, "command-value-error-change-not-set"));
			return true;
		}
		for(String arg : args)
		{
			builder.append(" ");
			builder.append(arg);
		}
		
		switch(menuUtility.getDataVariable())
		{
			case NAME -> mobHeadData.setName(builder.toString().strip());
			case DESCRIPTION -> mobHeadData.setDescription(builder.toString().strip());
			case DROPCHANCE ->
			{
				double value = argAsDouble(0);
				mobHeadData.setDropChance(value >= 0 && value <= 100 ? round(value, 2) : mobHeadData.getDropChance());
			}
			case TEXTURE ->
			{
					mobHeadData.setTexture(argAsString(0));
			}
			case FILENAME ->
			{
				String path = menuUtility.getLastPath() + "." + argAsString(0);
				Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
				if(MobHeadData.isValidHeadPath(config, path))
				{
					Message.msgPlayer(player, lang.getValue(player, "command-value-error-value-exists"));
					return true;
				}
				MobHeadData.createNewDirectory(config, menuUtility.getLastPath(), argAsString(0));
				new MenuPage(menuUtility, config, menuUtility.getLastPath(), null, menuUtility.getLastPage());
				return true;
			}
		}
		menuUtility.setMobHeadData(mobHeadData);
		menuUtility.setDataVariables(null);
		new ConfigurationPage(menuUtility, true).open();
		playerDataChange.remove(player);
		return true;
	}
	
	private List<String> sorted;
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		menuUtility = HeadMenuUtility.get(player);
		if(menuUtility == null)
		{
			return null;
		}
		if(menuUtility.getDataVariable() == MenuDataVariables.TEXTURE)
		{
			System.out.println("Texture");
			if(args.length == 1 && args[0].startsWith("Heads"))
			{
				String s = args[0];
				String[] parts = s.split("\\.");
				StringBuilder builder = new StringBuilder();
				long count = args[0].chars().filter(ch -> ch == '.').count();
				builder.append(parts[0]);
				for(int i = 1; i < (count == parts.length ? count : parts.length - 1); i++)
				{
					builder.append(".").append(parts[i]);
				}
				Set<String> stringSet = config.getSection(builder.toString(), false);
				if(stringSet.contains("customPath"))
				{
					return List.of(" ");
				}
				sorted = new ArrayList<>();
				StringUtil.copyPartialMatches(s, stringSet.stream().map(s1 -> builder + "." + s1).collect(Collectors.toList()), sorted);
				return sorted;
			}
			if(args.length > 1)
			{
				return List.of(" ");
			}
			return List.of("Heads");
		}
		
		//add auto completion for own head data, so you only need to type a name to get the texture value
		return null;
	}
	
	private double round(double value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
	public static void setPlayerDataChange(Player player, MobHeadData mobHeadData)
	{
		playerDataChange.put(player, mobHeadData);
	}
	
}