package com.wonkglorg;

import com.wonkglorg.utilitylib.config.Config;

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
	
	public MobHeadData(String name, String description, String texture, boolean enabled, double dropChance, String path)
	{
		this.name = name;
		this.description = description;
		this.texture = texture;
		this.enabled = enabled;
		this.dropChance = dropChance;
		this.path = path;
	}
	
	public MobHeadData(String path, Config config)
	{
		this.config = config;
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
	}
	
	public static List<MobHeadData> getAllValidConfigHeadData(Config config, String path)
	{
		List<MobHeadData> mobHeadData = new ArrayList<>();
		Set<String> subHeads = config.getSection(path, true);
		if(subHeads.isEmpty())
		{
			if(config.getBoolean(path + ".Enabled"))
			{
				mobHeadData.add(new MobHeadData(path, config));
			}
			return mobHeadData;
		}
		if(config.getBoolean(path + ".Enabled"))
		{
			mobHeadData.add(new MobHeadData(path, config));
		}
		for(String heads : subHeads)
		{
			if(config.getBoolean(path + "." + heads + ".Enabled"))
			{
				mobHeadData.add(new MobHeadData(path + "." + heads, config));
			}
		}
		return mobHeadData;
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
				if(config.getBoolean(newPath + ".Enabled") && config.getString(newPath + ".Texture") != null)
				{
					mobHeadData.add(new MobHeadData(newPath, config));
				}
				continue;
			}
			if(config.getBoolean(newPath + ".Enabled") && config.getString(newPath + ".Texture") != null)
			{
				mobHeadData.add(new MobHeadData(newPath, config));
				continue;
			}
			
			//skip to next head category if 1 matches
			for(String heads : subHeads)
			{
				if(config.getBoolean(newPath + "." + heads + ".Enabled") && config.getString(newPath + ".Texture") != null)
				{
					mobHeadData.add(new MobHeadData(newPath + "." + heads, config));
					break;
				}
			}
		}
		return mobHeadData;
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