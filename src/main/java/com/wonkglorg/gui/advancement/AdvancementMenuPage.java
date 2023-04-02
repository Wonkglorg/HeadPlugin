package com.wonkglorg.gui.advancement;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.PaginationGui;
import com.wonkglorg.utilitylib.inventory.Profile;
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
	
	public AdvancementMenuPage(JavaPlugin plugin, Profile menuUtility, AdvancementHandler handler)
	{
		super(54, "Advancements", plugin, menuUtility);
		this.plugin = plugin;
		this.handler = handler;
		pagination = new PaginationGui(this, FILLER);
		pagination.addSlots(1, 1, 4, 7);
	}
	
	@Override
	public void addComponents()
	{
		fill(FILLER);
		for(AdvancementData advancementData : handler.getAdvancements().values())
		{
			pagination.addPagedButton(addAdvancements(advancementData));
		}
		
		//check if someone is in the menu editing a value rn then block it for other users, keep track with a hashmap, and check them whenever a player opens this menu. otherwise you could ruin someone elses edits.ye I
	}
	
	private Button addAdvancements(AdvancementData advancement)
	{
		
		AdvancementDisplay display = advancement.getAdvancement().getDisplay();
		return new Button(ItemUtil.setName(display.getIcon(), display.getTitle().toString()))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				new AdvancementConfiguration(plugin,profile,advancement).open();
				destroy();
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