package com.wonkglorg.gui.advancement;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.MenuUtility;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementConfiguration extends InventoryGUI
{
	private final AdvancementData advancementData;
	
	public AdvancementConfiguration(JavaPlugin plugin, MenuUtility menuUtility, AdvancementData advancement)
	{
		super(54, advancement.getAdvancement().getDisplay().getTitle().toString(), plugin, menuUtility);
		this.advancementData = advancement;
	}
	
	@Override
	public void addComponents()
	{
	
	}
	
	private Button frameButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button criteriaButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button visibilityButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button textureButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button xButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button yButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
}