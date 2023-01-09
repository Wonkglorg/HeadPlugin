package com.wonkglorg.gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.gui.advancement.AdvancementMenuPage;
import com.wonkglorg.gui.heads.HeadMenuPage;
import com.wonkglorg.utilitylib.builder.ItemBuilder;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.MenuUtility;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HeadMenuGui extends InventoryGUI
{
	private final Heads plugin;
	
	public HeadMenuGui(Heads plugin, MenuUtility menuUtility)
	{
		super(9, "Head Gui", plugin, menuUtility);
		this.plugin = plugin;
	}
	
	@Override
	public void addComponents()
	{
		fill(0, 9	, FILLER);
		addButton(HeadDropConfigButton(), 1);
		addButton(AdvancementsButton(), 3);
		addButton(CloseButton(), 7);
	}
	
	private Button HeadDropConfigButton()
	{
		ItemStack icon = new ItemBuilder(Material.STONE).setName("Head Config").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				new HeadMenuPage(plugin,
						(HeadMenuUtility) menuUtility,
						plugin.manager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),
						"Heads",
						null,
						1).open();
			}
		};
	}
	
	private Button AdvancementsButton()
	{
		ItemStack icon;
		if(plugin.advancementApiExists())
		{
			icon = new ItemBuilder(Material.NETHER_BRICK).setName("Advancements").build();
		} else
		{
			icon = new ItemBuilder(Material.BEDROCK).setName(ChatColor.RED + "Advancements not installed").build();
		}
		
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(plugin.advancementApiExists())
				{
					new AdvancementMenuPage(plugin, menuUtility, plugin.getAdvancementHandler()).open();
				}
			}
		};
	}
	
	private Button CloseButton()
	{
		ItemStack icon = new ItemBuilder(Material.BARRIER).setName("Close").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				destroy();
				menuUtility.getOwner().closeInventory();
			}
		};
	}
}