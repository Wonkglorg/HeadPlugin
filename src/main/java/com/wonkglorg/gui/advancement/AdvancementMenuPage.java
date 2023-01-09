package com.wonkglorg.gui.advancement;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.MenuUtility;
import com.wonkglorg.utilitylib.inventory.PaginationGui;
import com.wonkglorg.utilitylib.item.ItemUtil;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementMenuPage extends InventoryGUI
{
	private final AdvancementHandler handler;
	private final PaginationGui pagination;
	private final JavaPlugin plugin;
	
	public AdvancementMenuPage(JavaPlugin plugin, MenuUtility menuUtility, AdvancementHandler handler)
	{
		super(54, "Advancements", plugin, menuUtility);
		this.plugin = plugin;
		this.handler = handler;
		pagination = new PaginationGui(this, FILLER);
		pagination.addSlots(2, 1, 4, 4);
	}
	
	@Override
	public void addComponents()
	{
		fill(0, 54, FILLER);
		for(AdvancementData advancementData : handler.getAdvancements().values())
		{
			pagination.addPagedButton(addAdvancements(advancementData));
		}
		//add all advancements to the list
		
		//first add value without parent if exists
		
		//Add all values to map and order by weather they are roots or not, then add the next layer etc
	}
	
	private Button addAdvancements(AdvancementData advancement)
	{
		
		AdvancementDisplay display = advancement.getAdvancement().getDisplay();
		return new Button(ItemUtil.setName(display.getIcon(), display.getTitle().toString()))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
			}
		};
	}
	
	private Button next()
	{
		ItemStack icon;
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				pagination.nextPage();
			}
		};
	}
	
	private Button previous()
	{
		ItemStack icon;
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				pagination.prevPage();
			}
		};
	}
	
	private Button create()
	{
		ItemStack icon;
		return new Button(null)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				//handler.addAdvancement();
			}
		};
	}
	
}