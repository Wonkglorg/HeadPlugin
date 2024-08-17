package com.wonkglorg.advancements;

import com.wonkglorg.utilitylib.manager.config.Config;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AdvancementData implements Cloneable
{
	private final StringAdvancement sAdvancement;
	private final Advancement advancement;
	private ItemStack icon;
	private boolean enabled;
	private final Map<String, String> criteriaLookupMap;
	
	public AdvancementData(StringAdvancement sAdvancement,
						   Advancement advancement,
						   ItemStack icon,
						   boolean enabled,
						   Map<String, String> criteriaLookupMap)
	{
		this.sAdvancement = sAdvancement;
		this.advancement = advancement;
		this.icon = icon;
		this.enabled = enabled;
		this.criteriaLookupMap = criteriaLookupMap;
	}
	
	public void saveToFile()
	{
		Config config = sAdvancement.getConfig();
		String path = sAdvancement.getPath();
		config.set(path + "." + "title", sAdvancement.getTitle());
		config.set(path + "." + "description", sAdvancement.getDescription());
		config.set(path + "." + "frame", sAdvancement.getFrame());
		config.set(path + "." + "visibility", sAdvancement.getVisibility());
		config.set(path + "." + "x", sAdvancement.getX());
		config.set(path + "." + "y", sAdvancement.getY());
		config.set(path + "." + "icon", sAdvancement.getIcon());
		config.set(path + "." + "parent", sAdvancement.getParent());
		config.set(path + "." + "advancementFlag", sAdvancement.getFlag());
		config.set(path + "." + "Enabled", enabled);
		criteriaLookupMap.forEach((s, s2) ->
		{
			config.set(path + "." + s2, s);
		});
		config.set(path + ".requirements", criteriaLookupMap.keySet());
		config.silentSave();
	}
	
	public static boolean isValid(@NotNull Config config, @NotNull String path)
	{
		return config.contains(path + ".title") &&
			   config.contains(path + ".description") &&
			   config.contains(path + ".frame") &&
			   config.contains(path + ".visibility") &&
			   config.contains(path + ".x") &&
			   config.contains(path + ".y") &&
			   config.contains(path + ".icon") &&
			   config.contains(path + ".advancementFlag") &&
			   config.contains(path + ".enabled") &&
			   config.contains(path + ".requirements");
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public void setIcon(ItemStack icon)
	{
		this.icon = icon;
	}
	
	public Map<String, String> getCriteriaLookupMap()
	{
		return criteriaLookupMap;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public ItemStack getIcon()
	{
		return icon;
	}
	
	public Advancement getAdvancement()
	{
		return advancement;
	}
	
	public StringAdvancement getsAdvancementString()
	{
		return sAdvancement;
	}
	
	@Override
	public AdvancementData clone()
	{
		try
		{
			return (AdvancementData) super.clone();
		} catch(CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
}