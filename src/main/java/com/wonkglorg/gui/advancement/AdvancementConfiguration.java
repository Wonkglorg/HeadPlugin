package com.wonkglorg.gui.advancement;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.Profile;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementConfiguration extends InventoryGUI
{
	private final AdvancementData advancementData;
	private int page = 1;
	
	public AdvancementConfiguration(JavaPlugin plugin, Profile menuUtility, AdvancementData advancement)
	{
		super(54, advancement.getsAdvancementString().getTitle(), plugin, menuUtility);
		this.advancementData = advancement;
	}
	
	@Override
	public void addComponents()
	{
		page1();
	}
	
	public void switchPage()
	{
		
		fill(0, 53, FILLER);
		
		if(page == 1)
		{
			page2();
		}
		
		if(page == 2)
		{
			page1();
		}
	}
	
	public void page2()
	{
		page = 2;
	}
	
	public void page1()
	{
		page = 1;
		addButton(4, enableButton());
		
		addButton(19, nameButton());
		addButton(22, requirementButton());
		addButton(25, descriptionButton());
		addButton(39, xButton());
		addButton(41, yButton());
		addButton(52, nextPageButton());
	}
	
	private Button frameButton()
	{
		return new Button(new ItemStack(Material.STRING))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	private Button nextPageButton()
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
	
	private Button enableButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				advancementData.setEnabled(!advancementData.isEnabled());
			}
		};
	}
	
	public Button nameButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	public Button descriptionButton()
	{
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			
			}
		};
	}
	
	public Button requirementButton()
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