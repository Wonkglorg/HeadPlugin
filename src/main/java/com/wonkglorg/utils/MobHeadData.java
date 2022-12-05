package com.wonkglorg.utils;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.item.ItemUtil;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
	private final Config config;
	private String fileName;
	private String path;
	private String originalName;
	
	public MobHeadData(String name,
					   String originalName,
					   String description,
					   String texture,
					   boolean enabled,
					   double dropChance,
					   String path,
					   Config config)
	{
		this.config = config;
		this.name = name;
		this.originalName = originalName;
		this.description = description;
		this.texture = texture;
		this.enabled = enabled;
		this.dropChance = dropChance;
		this.path = path;
	}
	
	public MobHeadData(String path, Config config, int originalNameOffset)
	{
		String[] pathParts = path.split("\\.");
		this.config = config;
		this.originalName = pathParts[originalNameOffset];
		this.name = config.getString(path + ".Name");
		this.description = config.getString(path + ".Description");
		this.texture = config.getString(path + ".Texture");
		this.enabled = config.getBoolean(path + ".Enabled");
		this.dropChance = config.getDouble(path + ".DropChance");
		this.fileName = pathParts[pathParts.length - 1];
		this.path = path;
	}
	
	public void setValuesFromHeadData(MobHeadData mobHeadData)
	{
		this.name = mobHeadData.getName();
		this.originalName = mobHeadData.getOriginalName();
		this.description = mobHeadData.description;
		this.texture = mobHeadData.getTexture();
		this.enabled = mobHeadData.isEnabled();
		this.dropChance = mobHeadData.getDropChance();
		this.path = mobHeadData.getPath();
	}
	
	public void writeToConfig()
	{
		setConfigTexture(config);
		setConfigName(config);
		setConfigDescription(config);
		setConfigEnabled(config);
		setConfigDropChance(config);
		config.silentSave();
	}
	
	public static void createNewDirectory(Config config, String path, String name)
	{
		String comPath = path + "." + name;
		if(isValidHeadPath(config, comPath))
		{
			return;
		}
		config.set(comPath + "." + "Name", "Enter value");
		config.set(comPath + "." + "Description", "Enter value");
		config.set(comPath + "." + "Texture", "Enter value");
		config.set(comPath + "." + "Enabled", true);
		config.set(comPath + "." + "DropChance", 100.0);
		config.silentSave();
	}
	
	private static MobHeadData toMobHeadData(String path, Config config, int offset)
	{
		return new MobHeadData(path, config, offset);
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
		mobHeadData.addAll(subHeads.stream()
								   .filter(subCategory -> isValidHeadPath(config, path + "." + subCategory))
								   .map(e -> new MobHeadData(path + "." + e, config, 1))
								   .toList());
		
		return mobHeadData;
	}
	
	public static void dropHead(String texture, String name, String description, Location loc)
	{
		loc.getWorld()
		   .dropItemNaturally(loc,
				   ItemUtil.createCustomHead(texture, name, description != null ? description.split("\\|") : new String[0]))
		   .getPersistentDataContainer()
		   .set(new NamespacedKey(Heads.getInstance(), "newdrop"), PersistentDataType.STRING, "true");
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
	
	public static MobHeadData randomHeadDrop(List<MobHeadData> mobHeadDataList)
	{
		WeightedRandomPicker<MobHeadData> weightedRandomPicker = new WeightedRandomPicker<>();
		if(mobHeadDataList.isEmpty())
		{
			return null;
		}
		for(MobHeadData mobHead : mobHeadDataList)
		{
			if(mobHead.getDropChance() > 0.0 && mobHead.isEnabled())
			{
				weightedRandomPicker.addEntry(mobHead, mobHead.getDropChance());
			}
		}
		
		if(weightedRandomPicker.getEntries().size() > 1)
		{
			if(weightedRandomPicker.getAccumulatedWeight() <= 100)
			{
				weightedRandomPicker.addEntry(null, 100 - weightedRandomPicker.getAccumulatedWeight());
			}
			return weightedRandomPicker.getRandom();
		}
		
		if(weightedRandomPicker.getAccumulatedWeight() > Math.random() * 100)
		{
			return mobHeadDataList.get(0);
		}
		return null;
	}
	
	public ItemStack createHeadItem()
	{
		return ItemUtil.createCustomHead(texture, name, description != null ? List.of(description.split("\\|")) : List.of(" "));
	}
	
	public ItemStack createHeadItemWithInfoDesc()
	{
		List<String> finishedDesc = new ArrayList<>();
		finishedDesc.add(enabled ? ChatColor.Reset + ChatColor.GREEN + "Enabled" : ChatColor.Reset + ChatColor.RED + "Disabled");
		finishedDesc.add(ChatColor.Reset + ChatColor.GOLD + "Dropchance: " + dropChance + "%");
		finishedDesc.addAll(description != null ? List.of(description.split("\\|")) : List.of(" "));
		return ItemUtil.createCustomHead(texture, name, finishedDesc);
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
		config.set(path + ".Texture", texture != null ? texture : " ");
	}
	
	private void setConfigName(Config config)
	{
		config.set(path + ".Name", name != null ? name : "Enter Name");
	}
	
	private void setConfigDescription(Config config)
	{
		config.set(path + ".Description", description != null ? description : "Enter Description");
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
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}