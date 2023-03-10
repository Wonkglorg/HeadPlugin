package com.wonkglorg.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.PersistentContainer;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.item.ItemUtil;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobHeadDataUtility
{
	
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
	
	public static MobHeadData randomHeadDrop(List<MobHeadData> mobHeadDataList, String world)
	{
		WeightedRandomPicker<MobHeadData> weightedRandomPicker = new WeightedRandomPicker<>();
		if(mobHeadDataList.isEmpty())
		{
			return null;
		}
		
		for(MobHeadData mobHead : mobHeadDataList)
		{
			if(mobHead.getWorlds() != null && !(mobHead.getWorlds().contains(world)))
			{
				if(!mobHead.getWorlds().contains("%all%"))
				{
					continue;
				}
			}
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
	
	private static MobHeadData toMobHeadData(String path, Config config, int offset)
	{
		return new MobHeadData(path, config, offset);
	}
	
	public static void dropHead(String texture, String name, String description, Location loc, String persistentData)
	{
		loc.getWorld()
		   .dropItemNaturally(loc,
				   ItemUtil.createCustomHead(texture, name, description != null ? description.split("\\|") : new String[0]))
		   .getPersistentDataContainer()
		   .set(PersistentContainer.PERSISTENT_ADVANCEMENT.getNamespaceKey(), PersistentDataType.STRING, persistentData);
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
	
	public static void updateYML()
	{
		Config config = Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
		for(MobHeadData mobHeadData : getAllValidConfigHeadData(config, "Heads"))
		{
			String path = mobHeadData.getPath();
			if(!config.contains(path + ".Worlds"))
			{
				config.set(path + ".Worlds", "%all%");
			}
			if(!config.contains(path + ".Permission"))
			{
				config.set(path + ".Permission", "%non%");
			}
			if(!config.contains(path + ".Sound"))
			{
				config.set(path + ".Sound", "ENTITY_" + mobHeadData.getOriginalName().toUpperCase() + "_AMBIENT");
			}
		}
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
	
	public static boolean isValidHeadPath(Config config, String path)
	{
		return config.contains(path + ".Enabled") && config.contains(path + ".Texture") && config.contains(path + ".DropChance") && config.contains(
				path + ".Name") && config.contains(path + ".Description");
	}
	
	public static void createNewDirectory(Config config, String path, String name)
	{
		String comPath = path + "." + name;
		if(isValidHeadPath(config, comPath))
		{
			return;
		}
		config.set(comPath + ".Name", "Enter value");
		config.set(comPath + ".Description", "Enter value");
		config.set(comPath + ".Texture", "Enter value");
		config.set(comPath + ".Enabled", true);
		config.set(comPath + ".DropChance", 100.0);
		config.set(comPath + ".Worlds", "%all%");
		config.set(comPath + ".Permission", "%non%");
		config.set(comPath + ".Sound", " ");
		config.silentSave();
	}
	
}