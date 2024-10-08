package com.wonkglorg.command.value;

import com.wonkglorg.Heads;
import com.wonkglorg.command.value.valuehandler.HeadDescriptionValueHandler;
import com.wonkglorg.command.value.valuehandler.HeadDropChanceHandler;
import com.wonkglorg.command.value.valuehandler.HeadFileValueHandler;
import com.wonkglorg.command.value.valuehandler.HeadNameValueHandler;
import com.wonkglorg.command.value.valuehandler.HeadTextureValueHandler;
import com.wonkglorg.command.value.valuehandler.HeadValueHandler;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.gui.heads.ConfigurationPage;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.base.message.Message;
import com.wonkglorg.utilitylib.manager.command.Command;
import com.wonkglorg.utilitylib.manager.config.Config;
import com.wonkglorg.utilitylib.manager.managers.LangManager;
import com.wonkglorg.utils.HeadProfile;
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
	private final LangManager lang = Heads.manager().getLangManager();
	private final Config config = Heads.manager().getConfigManager().getConfig(YML.HEAD_DATA_BACKUP.getFileName());
	
	private final Map<MenuDataVariables, HeadValueHandler> variableHandlerMap = new HashMap<>();
	private HeadProfile headProfile;
	
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public ChangeValueCommand(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
		initializeMenuDataMap();
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(Player player, String[] args)
	{
		if(args.length == 0)
		{
			Message.msgPlayer(player, lang.getValue(player, "command-value-error-null-input"));
			return true;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("quit"))
		{
			headProfile.setDataVariables(null);
			new ConfigurationPage((Heads) plugin, headProfile, true).open();
			playerDataChange.remove(player);
			return true;
		}
		
		playerDataChange.keySet().removeIf(Predicate.not(Player::isValid));
		
		MobHeadData mobHeadData = playerDataChange.get(player);
		this.headProfile = Heads.getProfileManager().get(player);
		if(headProfile == null || headProfile.getDataVariable() == null)
		{
			Message.msgPlayer(player, lang.getValue(player, "command-value-error-change-not-set"));
			return true;
		}
		StringBuilder builder = new StringBuilder();
		for(String arg : args)
		{
			builder.append(" ");
			builder.append(arg);
		}
		variableHandlerMap.get(headProfile.getDataVariable()).accept(player, config, mobHeadData, builder.toString().trim());
		
		playerDataChange.remove(player);
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		headProfile = Heads.getProfileManager().get(player);
		if(headProfile == null)
		{
			return null;
		}
		if(headProfile.getDataVariable() == MenuDataVariables.TEXTURE)
		{
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
				List<String> sorted = new ArrayList<>();
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
	
	public static void setPlayerDataChange(Player player, MobHeadData mobHeadData)
	{
		playerDataChange.put(player, mobHeadData);
	}
	
	private void initializeMenuDataMap()
	{
		variableHandlerMap.put(MenuDataVariables.FILENAME, new HeadFileValueHandler());
		variableHandlerMap.put(MenuDataVariables.NAME, new HeadNameValueHandler());
		variableHandlerMap.put(MenuDataVariables.DESCRIPTION, new HeadDescriptionValueHandler());
		variableHandlerMap.put(MenuDataVariables.TEXTURE, new HeadTextureValueHandler());
		variableHandlerMap.put(MenuDataVariables.DROPCHANCE, new HeadDropChanceHandler());
	}
	
}