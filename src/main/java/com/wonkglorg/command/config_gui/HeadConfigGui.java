package com.wonkglorg.command.config_gui;

import com.wonkglorg.MobHeadData;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.MenuUtility;
import com.wonkglorg.utilitylib.utils.inventory.PaginationGui;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadConfigGui extends PaginationGui
{
	private List<MobHeadData> mobHeadData;
	private final Map<Button, MobHeadData> mobHeadDataMap;
	private final Config config;
	private final HeadMenuUtility menuUtility;
	
	public HeadConfigGui(InventoryGUI gui, HeadMenuUtility menuUtility, Config config)
	{
		super(gui);
		this.config = config;
		mobHeadDataMap = new HashMap<>();
		this.menuUtility = menuUtility;
		mobHeadData = MobHeadData.getFirstOfAllValidConfigHeadData(config, "Heads");
		
		addSlots(1, 1, 8, 4);
		
		for(MobHeadData mobHeadData1 : mobHeadData)
		{
			Button button = headButton(ItemUtility.createCustomHead(mobHeadData1.getTexture(), mobHeadData1.getName()));
			mobHeadDataMap.put(button, mobHeadData1);
			addPagedButton(button);
		}
		
		gui.addButton(53, forward(new ItemStack(Material.ARROW)));
		gui.addButton(52, backwards(new ItemStack(Material.FEATHER)));
		gui.open();
		updatePage();
	}
	
	private Button forward(ItemStack itemStack)
	{
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				nextPage();
			}
		};
	}
	
	private Button backwards(ItemStack itemStack)
	{
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				prevPage();
			}
		};
	}
	
	private Button headButton(ItemStack itemStack)
	{
		
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				clear();
				setPage(1);
				mobHeadData = MobHeadData.getAllValidConfigHeadData(config, mobHeadDataMap.get(this).getPath());
				
				for(MobHeadData mobHeadData1 : mobHeadData)
				{
					addPagedButton(configMenu(ItemUtility.createCustomHead(mobHeadData1.getTexture(),mobHeadData1.getName()),menuUtility,mobHeadData1));
				}
			}
		};
	}
	
	private Button configMenu(ItemStack itemStack, HeadMenuUtility menuUtility, MobHeadData mobHeadData)
	{
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				menuUtility.setMobHeadData(mobHeadData);
				new HeadConfigurationPage(menuUtility).open();
			}
		};
	}
	
	private void returnButton()
	{
		//add return method button to to always go 1 step back and keep all info, what page I was on, what mob etc
	}
}