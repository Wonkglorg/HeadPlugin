package com.wonkglorg.utils;

import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobHeadData
{
	private String name;
	private String description;
	private String texture;
	private boolean enabled;
	private double dropChance;
	private Config config;
	
	private String path;
	
	private String originalName;
	
	public MobHeadData(String name, String originalName, String description, String texture, boolean enabled, double dropChance, String path)
	{
		this.name = name;
		this.originalName = originalName;
		this.description = description;
		this.texture = texture;
		this.enabled = enabled;
		this.dropChance = dropChance;
		this.path = path;
	}
	
	public MobHeadData(String path, Config config, int offset)
	{
		this.config = config;
		this.originalName = path.split("\\.")[offset];
		this.name = config.getString(path + ".Name");
		this.description = config.getString(path + ".Description");
		this.texture = config.getString(path + ".Texture");
		this.enabled = config.getBoolean(path + ".Enabled");
		this.dropChance = config.getDouble(path + ".DropChance");
		this.path = path;
	}
	
	public void setValues()
	{
		setConfigTexture(config);
		setConfigName(config);
		setConfigDescription(config);
		setConfigEnabled(config);
		setConfigDropChance(config);
		config.silentSave();
	}
	
	public List<String> splitPathIntoSections(Config config, String path)
	{
		String[] splitPath = path.split("\\.");
		List<String> stringList = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for(String s : splitPath)
		{
			builder.append(s);
			if(!isValidHeadPath(config, builder.toString()))
			{
				stringList.add(builder.toString());
				builder.append(".");
			}
		}
		return stringList;
		
	}
	
	public static List<MobHeadData> getAllValidConfigHeadData(Config config, String path)
	{
		List<MobHeadData> mobHeadData = new ArrayList<>();
		Set<String> subHeads = config.getSection(path, true);
		if(subHeads.isEmpty())
		{
			if(isValidHeadPath(config, path))
			{
				mobHeadData.add(new MobHeadData(path, config, 1));
			}
			return mobHeadData;
		}
		if(isValidHeadPath(config, path))
		{
			mobHeadData.add(new MobHeadData(path, config, 1));
		}
		for(String heads : subHeads)
		{
			if(isValidHeadPath(config, path + "." + heads))
			{
				mobHeadData.add(new MobHeadData(path + "." + heads, config, 1));
			}
		}
		return mobHeadData;
	}
	
	public static boolean isValidHeadPath(Config config, String path)
	{
		return config.contains(path + ".Enabled") && config.contains(path + ".Texture") && config.contains(path + ".DropChance") && config.contains(
				path + ".Name") && config.contains(path + ".Description");
	}
	
	public static MobHeadData getFirstValidConfigHeadData(Config config, String path)
	{
		String newPath;
		for(String categories : config.getSection(path, false))
		{
			newPath = path + "." + categories;
			Set<String> subHeads = config.getSection(newPath, true);
			if(subHeads.isEmpty())
			{
				if(isValidHeadPath(config, newPath))
				{
					return new MobHeadData(newPath, config, 1);
				}
				continue;
			}
			if(isValidHeadPath(config, newPath))
			{
				return new MobHeadData(newPath, config, 1);
			}
			for(String heads : subHeads)
			{
				if(isValidHeadPath(config, newPath + "." + heads))
				{
					return new MobHeadData(newPath + "." + heads, config, 1);
				}
			}
		}
		return null;
	}
	
	public static List<MobHeadData> getFirstOfAllValidConfigHeadData(Config config, String path)
	{
		List<MobHeadData> mobHeadData = new ArrayList<>();
		String newPath;
		for(String categories : config.getSection(path, false))
		{
			newPath = path + "." + categories;
			Set<String> subHeads = config.getSection(newPath, true);
			if(subHeads.isEmpty())
			{
				if(isValidHeadPath(config, newPath))
				{
					mobHeadData.add(new MobHeadData(newPath, config, 1));
				}
				continue;
			}
			if(isValidHeadPath(config, newPath))
			{
				mobHeadData.add(new MobHeadData(newPath, config, 1));
				continue;
			}
			for(String heads : subHeads)
			{
				if(isValidHeadPath(config, newPath + "." + heads))
				{
					mobHeadData.add(new MobHeadData(newPath + "." + heads, config, 1));
					break;
				}
			}
		}
		return mobHeadData;
	}
	
	public ItemStack createHeadItem()
	{
		return ItemUtility.createCustomHead(texture, name, description);
	}
	
	public ItemStack createHeadItemWithInfoDesc()
	{
		String type = enabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";

		return ItemUtility.createCustomHead(texture,
				name,
				ChatColor.Reset + ChatColor.GOLD + "Dropchance: " + dropChance + "%",
				type ,
				description);
	}
	
	public String getOriginalName()
	{
		return originalName;
	}
	
	public void setOriginalName(String originalName)
	{
		this.originalName = originalName;
	}
	
	private void setConfigTexture(Config config)
	{
		config.set(path + ".Texture", texture);
	}
	
	private void setConfigName(Config config)
	{
		config.set(path + ".Name", name);
	}
	
	private void setConfigDescription(Config config)
	{
		config.set(path + ".Description", description);
	}
	
	private void setConfigEnabled(Config config)
	{
		config.set(path + ".Enabled", enabled);
	}
	
	private void setConfigDropChance(Config config)
	{
		config.set(path + ".DropChance", dropChance);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public double getDropChance()
	{
		return dropChance;
	}
	
	public void setDropChance(double dropChance)
	{
		this.dropChance = dropChance;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
}