package com.wonkglorg.command.dropchance;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.heads.MobHeadDataUtility;
import com.wonkglorg.utilitylib.base.message.ChatColor;
import com.wonkglorg.utilitylib.base.message.Message;
import com.wonkglorg.utilitylib.manager.command.Command;
import com.wonkglorg.utilitylib.manager.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
	Config config = Heads.manager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
	
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
		
		List<MobHeadData> mobHeadDataList = MobHeadDataUtility.getAllValidConfigHeadData(config, argAsString(0));
		
		int finalAmount1 = amount;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Map<String, Integer> resultMap = roll(mobHeadDataList, finalAmount1);
				sendResult(resultMap, finalAmount1, player);
			}
		}.runTask(plugin);
		
		return true;
	}
	
	public Map<String, Integer> roll(List<MobHeadData> mobHeadDataList, int amount)
	{
		Map<String, Integer> rollMap = new HashMap<>();
		
		for(int i = 0; i < amount; i++)
		{
			MobHeadData mobHeadData = MobHeadDataUtility.randomHeadDrop(mobHeadDataList);
			if(mobHeadData == null)
			{
				rollMap.put("nothing", (rollMap.get("nothing") != null ? rollMap.get("nothing") : 0) + 1);
				continue;
			}
			rollMap.put(mobHeadData.getName(), (rollMap.get(mobHeadData.getName()) != null ? rollMap.get(mobHeadData.getName()) : 0) + 1);
			
		}
		return rollMap;
	}
	
	public void sendResult(Map<String, Integer> resultMap, int amount, Player player)
	{
		List<MobDropData> mobDropDataList = new ArrayList<>();
		for(String s : resultMap.keySet())
		{
			mobDropDataList.add(new MobDropData(resultMap.get(s), s));
		}
		mobDropDataList.sort((o1, o2) -> Double.compare(o2.getPercent(amount), o1.getPercent(amount)));
		Message.msgPlayer(player, ChatColor.Reset + ChatColor.GOLD + "Total rolled " + ChatColor.YELLOW + amount);
		
		Message.msgPlayer(player, ChatColor.Reset + ChatColor.GOLD + "%     " + ChatColor.LIGHT_PURPLE + "Amount      " + ChatColor.BLUE + "Name");
		for(MobDropData mobDropData : mobDropDataList)
		{
			Message.msgPlayer(player,
					ChatColor.Reset +
					ChatColor.GOLD +
					round(mobDropData.getPercent(amount), 2) +
					" " +
					ChatColor.LIGHT_PURPLE +
					(int) mobDropData.getAmount() +
					" " +
					ChatColor.BLUE +
					mobDropData.getName());
		}
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