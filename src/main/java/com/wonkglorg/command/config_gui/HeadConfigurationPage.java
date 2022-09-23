package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.MobHeadData;
import com.wonkglorg.utilitylib.utils.Utils;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.MenuUtility;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HeadConfigurationPage extends InventoryGUI
{
	
	private String texture;
	private String name;
	private String description;
	private double dropchance;
	public HeadConfigurationPage(HeadMenuUtility menuUtility)
	{
		super(54, menuUtility.getMobHeadData().getName(), Heads.getInstance(), menuUtility);
		MobHeadData headData = menuUtility.getMobHeadData();
		texture = headData.getTexture();
		name = headData.getName();
		description = headData.getDescription();
		dropchance = headData.getDropChance();
	}
	
	@Override
	public void addComponents()
	{
		fill(0, super.getInventory().getSize(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
		addButton(HeadShowcase(texture,name,description),4);
	}
	
	
	public Button HeadShowcase(String texture,String name,String... description){
		return new Button(ItemUtility.createCustomHead(texture,name,description)){
			@Override
			public void onClick(InventoryClickEvent e)
			{
				Utils.give((Player) e.getWhoClicked(),getItem());
			}
		};
	}
	
	
	//add change buttons store temp head data until apply button is clicked
	
}