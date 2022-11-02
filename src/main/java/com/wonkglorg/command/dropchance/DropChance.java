package com.wonkglorg.command.dropchance;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.message.Message;
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
import java.util.stream.Collectors;

public class DropChance extends Command
{
	Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
	
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public DropChance(@NotNull JavaPlugin plugin, @NotNull String name)
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
		
		//split amount between threads and collect result afterwards
		
		//notify seperate threat that all are finished and then send message
		int amount = 1;
		
		Map<String, Integer> rolledMap = new HashMap<>();
		if(args.length == 1)
		{
			return false;
		}
		if(argAsString(0) == null)
		{
			return false;
		}
		if(args.length == 2)
		{
			amount = argAsInteger(1, 1);
		}
		
		List<MobHeadData> mobHeadDataList = MobHeadData.getAllValidConfigHeadData(config, argAsString(0));
		
		for(int i = 0; i < amount; i++)
		{
			MobHeadData mobHeadData = MobHeadData.randomHeadDrop(mobHeadDataList);
			if(mobHeadData == null)
			{
				rolledMap.put("nothing", (rolledMap.get("nothing") != null ? rolledMap.get("nothing") : 0) + 1);
				continue;
			}
			rolledMap.put(mobHeadData.getName(), (rolledMap.get(mobHeadData.getName()) != null ? rolledMap.get(mobHeadData.getName()) : 0) + 1);
			
		}
		List<MobDropData> mobDropDataList = new ArrayList<>();
		for(String s : rolledMap.keySet())
		{
			mobDropDataList.add(new MobDropData(rolledMap.get(s), s));
		}
		int finalAmount = amount;
		mobDropDataList.sort((o1, o2) -> Double.compare(o2.getPercent(finalAmount), o1.getPercent(finalAmount)));
		Message.msgPlayer(player, ChatColor.Reset + ChatColor.GOLD+ "Total rolled " +ChatColor.YELLOW+  amount);

		Message.msgPlayer(player, ChatColor.Reset + ChatColor.GOLD + "%     " + ChatColor.LIGHT_PURPLE + "Amount      " + ChatColor.BLUE + "Name");
		for(MobDropData mobDropData : mobDropDataList)
		{
			Message.msgPlayer(player,
					ChatColor.Reset +
					ChatColor.GOLD +
					round(mobDropData.getPercent(amount), 2) +
					" " +
					ChatColor.LIGHT_PURPLE +
					(int)mobDropData.getAmount() +
					" " +
					ChatColor.BLUE +
					mobDropData.getName());
		}
		return true;
	}
	
	List<String> empty = new ArrayList<>();
	List<String> sorted;
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		if(args.length == 1 && args[0].startsWith("Heads."))
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
				return empty;
			}
			sorted = new ArrayList<>();
			StringUtil.copyPartialMatches(s, stringSet.stream().map(s1 -> builder + "." + s1).collect(Collectors.toList()), sorted);
			return sorted;
		}
		if(args.length > 1)
		{
			return empty;
		}
		return List.of("Heads");
	}
	
	private static class MobDropData
	{
		private double amount;
		private String name;
		
		public MobDropData(int amount, String name)
		{
			this.amount = amount;
			this.name = name;
		}
		
		public double getAmount()
		{
			return amount;
		}
		
		public String getName()
		{
			return name;
		}
		
		public double getPercent(int max)
		{
			return (amount / max) * 100.0;
		}
		
		public void setAmount(int amount)
		{
			this.amount = amount;
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
	}
	
	private double round(double value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
}