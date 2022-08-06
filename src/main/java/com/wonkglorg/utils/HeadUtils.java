package com.wonkglorg.utils;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.Location;

public class HeadUtils
{
	public static void dropHead(String texture, String name, String description, Location loc)
	{
		String configTexture = readConfigString(YML.HEAD_DATA, texture);
		String configName = readConfigString(YML.HEAD_DATA, name);
		String configDescription = readConfigString(YML.HEAD_DATA, description);
		String[] descriptionArray;
		if(configDescription != null){
			descriptionArray = configDescription.split("\\|");
		}
		else{
			descriptionArray = new String[0];
		}
		if(configTexture==null || configTexture.isEmpty()){
			configTexture = "";
		}
		
		loc.getWorld().dropItemNaturally(loc, ItemUtility.createCustomHead(configTexture, configName, descriptionArray));
	}
	
	public static String readConfigString(YML yml, String path)
	{
		return Heads.getManager().getConfig(yml.getFileName()).getString(path);
	}
	
	public static boolean readConfigBoolean(YML yml, String path)
	{
		return Heads.getManager().getConfig(yml.getFileName()).getBoolean(path);
	}
	
	public static int readConfigInt(YML yml, String path)
	{
		return Heads.getManager().getConfig(yml.getFileName()).getInt(path);
	}
	
	
	
}