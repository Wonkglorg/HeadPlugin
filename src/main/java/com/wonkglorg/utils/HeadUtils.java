package com.wonkglorg.utils;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.managers.ConfigManager;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public class HeadUtils
{
	
	private static final ConfigManager configManager = Heads.getPluginManager().getConfigManager();
	
	public static void dropHead(String texture, String name, String description, Location loc)
	{
		String[] descriptionArray;
		descriptionArray = description != null ? description.split("\\|") : new String[0];
		loc.getWorld()
		   .dropItemNaturally(loc, ItemUtility.createCustomHead(texture, name, descriptionArray))
		   .getPersistentDataContainer()
		   .set(new NamespacedKey(Heads.getInstance(), "newdrop"), PersistentDataType.STRING,"true");
	}
	
	public static boolean readConfigBoolean(YML yml, String path)
	{
		return configManager.getConfig(yml.getFileName()).getBoolean(path);
	}
	
}