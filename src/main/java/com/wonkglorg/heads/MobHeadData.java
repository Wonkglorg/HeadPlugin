package com.wonkglorg.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.item.ItemUtil;
import com.wonkglorg.utilitylib.message.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
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
	private Set<String> worlds;
	private Set<String> permissions;
	private Sound sound;
	
	
	
	//show player list of all possible sounds with search feature to narrow it down?
	
	//so typing cave results in all values returned with cave in the name
	
	//then just use value.of(button name)
	
	//do not use sql, not worth it for current purpose, stay with flatfile
	
	public MobHeadData(String name,
					   String originalName,
					   String description,
					   String texture,
					   boolean enabled,
					   double dropChance,
					   String path,
					   Set<String> worlds,
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
		this.worlds = worlds;
	}
	
	public MobHeadData(String path, Config config, int originalNameOffset)
	{
		Sound.ENTITY_SQUID_AMBIENT
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
		this.worlds = new HashSet<>(config.getStringList(path + ".Worlds"));
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
		this.worlds = mobHeadData.getWorlds();
	}
	
	public void writeToConfig()
	{
		setConfigTexture(config);
		setConfigName(config);
		setConfigDescription(config);
		setConfigEnabled(config);
		setConfigDropChance(config);
		setWorlds(config);
		config.silentSave();
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
		config.set(comPath + ".", new ArrayList<String>());
		config.silentSave();
	}
	
	public static boolean isValidHeadPath(Config config, String path)
	{
		return config.contains(path + ".Enabled") && config.contains(path + ".Texture") && config.contains(path + ".DropChance") && config.contains(
				path + ".Name") && config.contains(path + ".Description");
	}
	
	public static boolean isValidHeadPath(Config config, String path, Set<String> worlds)
	{
		return config.contains(path + ".Enabled") && config.contains(path + ".Texture") && config.contains(path + ".DropChance") && config.contains(
				path + ".Name") && config.contains(path + ".Description");
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
		if(worlds.contains("%all%"))
		{
			finishedDesc.add("Worlds: ALL");
		} else
		{
			finishedDesc.add("Worlds: " + worlds);
		}
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
	
	private void setWorlds(Config config)
	{
		if(worlds == null)
		{
			config.set(path + ".Worlds", new ArrayList<String>());
			return;
		}
		config.set(path + ".Worlds", worlds);
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
	
	public Set<String> getWorlds()
	{
		if(Heads.getManager().getConfigManager().getConfig(YML.CONFIG.getFileName()).getBoolean("WorldRestrictedHeads"))
		{
			return worlds;
		}
		return null;
	}
	
	public void setWorlds(Set<String> worlds)
	{
		this.worlds = worlds;
	}
}