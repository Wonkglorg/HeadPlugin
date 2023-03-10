package com.wonkglorg.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.item.ItemUtil;
import com.wonkglorg.utilitylib.message.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
					   Set<String> permissions,
					   Sound sound,
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
		this.permissions = permissions;
		this.sound = sound;
	}
	
	public MobHeadData(String path, Config config, int originalNameOffset)
	{
		String[] pathParts = path.split("\\.");
		this.config = config;
		setOriginalName(pathParts[originalNameOffset]);
		setName(config.getString(path + ".Name"));
		setDescription(config.getString(path + ".Description"));
		setTexture(config.getString(path + ".Texture"));
		setEnabled(config.getBoolean(path + ".Enabled"));
		setDropChance(config.getDouble(path + ".DropChance"));
		setFileName(pathParts[pathParts.length - 1]);
		setPath(path);
		setWorlds(config.getString(path + ".Worlds"));
		setPermissions(config.getString(path + ".Permission"));
		setSound(config.getString(path + ".Sound"));
	}
	
	public void setValuesFromHeadData(MobHeadData mobHeadData)
	{
		setOriginalName(mobHeadData.getOriginalName());
		setName(mobHeadData.getName());
		setDescription(mobHeadData.getDescription());
		setTexture(mobHeadData.getTexture());
		setEnabled(mobHeadData.isEnabled());
		setDropChance(mobHeadData.getDropChance());
		setPath(mobHeadData.getPath());
		setWorlds(mobHeadData.getWorlds());
		setSound(mobHeadData.getSound());
		
	}
	
	public void writeToConfig()
	{
		setConfigTexture(config);
		setConfigName(config);
		setConfigDescription(config);
		setConfigEnabled(config);
		setConfigDropChance(config);
		setConfigWorlds(config);
		setConfigPermission(config);
		setConfigSound(config);
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
	
	private void setConfigWorlds(Config config)
	{
		if(worlds == null)
		{
			config.set(path + ".Worlds", "");
			return;
		}
		config.set(path + ".Worlds", worlds);
		
		//convert back to singular string with all seperated by ","
	}
	
	private void setConfigPermission(Config config)
	{
		//convert back to singular string with all seperated by ","
	}
	
	private void setConfigSound(Config config)
	{
		config.set(path + ".Sound", sound.toString());
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
	
	public void setWorlds(String worlds)
	{
		if(worlds == null)
		{
			this.worlds = new HashSet<>(List.of("%all%"));
			return;
		}
		this.worlds = new HashSet<>(Arrays.stream(worlds.split(",")).toList());
	}
	
	public Set<String> getPermissions()
	{
		return permissions;
	}
	
	public Sound getSound()
	{
		return sound;
	}
	
	public void setPermissions(Set<String> permissions)
	{
		this.permissions = permissions;
	}
	
	public void setPermissions(String permissions)
	{
		if(permissions == null){
			this.permissions = new HashSet<>(List.of(" "));
			return;
		}
		this.permissions = new HashSet<>(Arrays.stream(permissions.split(",")).toList());
	}
	
	public void setSound(@NotNull Sound sound)
	{
		this.sound = sound;
	}
	
	public void setSound(String sound)
	{
		try
		{
			this.sound = Sound.valueOf(sound);
		} catch(Exception e)
		{
			this.sound = Sound.ENTITY_VILLAGER_AMBIENT;
		}
	}
}