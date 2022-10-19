package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ChangeValueCommand extends Command
{
	
	private static final Map<Player, MobHeadData> playerDataChange = new HashMap<>();
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
			Message.msgPlayer(player, "Unknown value to edit");
			return true;
		}
		if(args.length == 0)
		{
			Message.msgPlayer(player, "Null is not a valid value");
			return true;
		}
		
		playerDataChange.keySet().removeIf(Predicate.not(Player::isValid));
		
		MobHeadData mobHeadData = playerDataChange.get(player);
		HeadMenuUtility menuUtility = HeadMenuUtility.get(player);
		this.menuUtility = menuUtility;
		StringBuilder builder = new StringBuilder();
		
		if(menuUtility == null || menuUtility.getDataVariable() == null)
		{
			Message.msgPlayer(player, "Unknown value to edit");
			return true;
		}
		for(String arg : args)
		{
			builder.append(" ");
			builder.append(arg);
		}
		System.out.println(builder);
		switch(menuUtility.getDataVariable())
		{
			case NAME -> mobHeadData.setName(builder.toString().strip());
			case DESCRIPTION -> mobHeadData.setDescription(builder.toString().strip());
			case DROPCHANCE ->
			{
				double value = argAsDouble(0);
				mobHeadData.setDropChance(value >= 0 && value <= 100 ? round(value, 2) : mobHeadData.getDropChance());
			}
			case TEXTURE -> mobHeadData.setTexture(argAsString(1));
			case FILENAME ->
			{
				String path = menuUtility.getLastPath() + "." + argAsString(0);
				Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
				if(MobHeadData.isValidHeadPath(config, path))
				{
					Message.msgPlayer(player, "Value already exists");
					return true;
				}
				MobHeadData.createNewDirectory(config,menuUtility.getLastPath(),argAsString(0));
				new MenuPage(menuUtility, config, menuUtility.getLastPath(), null);
				return true;
			}
		}
		menuUtility.setMobHeadData(mobHeadData);
		menuUtility.setDataVariables(null);
		new ConfigurationPage(menuUtility, true).open();
		playerDataChange.remove(player);
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		if(menuUtility == null)
		{
			return null;
		}
		if(menuUtility.getDataVariable() != MenuDataVariables.TEXTURE)
		{
			return null;
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