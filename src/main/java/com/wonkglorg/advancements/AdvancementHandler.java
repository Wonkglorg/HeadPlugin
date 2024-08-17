package com.wonkglorg.advancements;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.manager.config.Config;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Stack;

public class AdvancementHandler
{
	private final Heads plugin;
	private final AdvancementManager manager = new AdvancementManager(new NameKey("WonkyHeads", "manager"));
	private final HashMap<String, AdvancementData> advancements = new HashMap<>();
	private final Stack<AdvancementData> advancementQueue = new Stack<>();
	
	public AdvancementHandler(Heads plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Loads all advancements into memory and adds them to the server
	 *
	 * @param config
	 */
	public void startup(Config config)
	{
		addAdvancements(config);
	}
	
	public void addAdvancements(Config config)
	{
		String path = "advancements";
		for(String section : config.getSection(path, false))
		{
			System.out.println("----------------------------------------------------------");
			if(advancements.containsKey(section))
			{
				System.out.println("Already added");
				continue;
			}
			path = "advancements";
			path = path + "." + section;
			System.out.println("Path " + path);
			if(!AdvancementData.isValid(config, path))
			{
				System.out.println("Not valid");
				continue;
			}
			
			AdvancementData advancementData = new AdvancementBuilder(section, path, config).setValuesFromConfig().build();
			System.out.println("AdvancementData " + advancementData);
			checkParent(config, advancementData);
			System.out.println("AdvancementQueue " + advancementQueue);
			while(!advancementQueue.isEmpty())
			{
				AdvancementData advancement = advancementQueue.pop();
				advancements.put(advancement.getsAdvancementString().getKey(), advancement);
			}
			advancements.put(section, advancementData);
		}
		
		for(AdvancementData data : advancements.values())
		{
			manager.addAdvancement(data.getAdvancement());
		}
		
	}
	
	/**
	 * checks if a parent exists and if yes if it is already initialized
	 *
	 * @param config
	 * @param advancementData
	 */
	private void checkParent(Config config, AdvancementData advancementData)
	{
		String parent = advancementData.getsAdvancementString().getParent();
		if(parent == null)
		{
			return;
		}
		if(advancements.containsKey(parent))
		{
			return;
		}
		AdvancementData parentData = new AdvancementBuilder(parent,advancementData.getsAdvancementString().getPath(),config).setValuesFromConfig().build();
		advancementQueue.add(parentData);
		checkParent(config,parentData);
	}
	
	public static void createNewDirectory(Config config, String path, String name)
	{
		String comPath = path + "." + name;
		if(config.contains(comPath))
		{
			return;
		}
		config.set(comPath + "." + "title", "Enter value");
		config.set(comPath + "." + "description", "Enter value");
		config.set(comPath + "." + "frame", "TASK");
		config.set(comPath + "." + "visibility", "ALWAYS");
		config.set(comPath + "." + "x", 1);
		config.set(comPath + "." + "y", 1);
		config.set(comPath + "." + "icon", "Heads.Sheep.Red");
		config.set(comPath + "." + "parent", null);
		config.set(comPath + "." + "advancementFlag", "SHOW_TOAST");
		config.set(comPath + "." + "Enabled", true);
		config.set(comPath + "." + "criteria", " ");
		config.set(comPath + "." + "requirements", " ");
		config.silentSave();
	}
	
	public AdvancementManager getManager()
	{
		return manager;
	}
	
	public HashMap<String, AdvancementData> getAdvancements()
	{
		return advancements;
	}
	
	public void grantAdvancementCriteria(Player player, String value)
	{
		
		String[] criteriaList = value.split("\\.");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(AdvancementData advancementData : advancements.values())
				{
					Advancement advancement = advancementData.getAdvancement();
					if(advancement.isGranted(player))
					{
						continue;
					}
					
					StringBuilder builder = new StringBuilder();
					for(String s : criteriaList)
					{
						if(builder.isEmpty())
						{
							builder.append(s);
						} else
						{
							builder.append(".").append(s);
						}
						String finalCriteria = builder.toString().toLowerCase();
						
						String result = advancementData.getCriteriaLookupMap().get(finalCriteria);
						
						if(result == null)
						{
							continue;
						}
						manager.grantCriteria(player, advancement, result);
						manager.saveProgress(player, advancement);
					}
				}
			}
		}.runTask(plugin);
	}
	
}