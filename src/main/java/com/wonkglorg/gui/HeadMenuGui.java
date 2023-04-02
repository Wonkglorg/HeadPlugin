package com.wonkglorg.gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.gui.advancement.AdvancementMenuPage;
import com.wonkglorg.gui.heads.HeadMenuPage;
import com.wonkglorg.utilitylib.builder.ItemBuilder;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.Profile;
import com.wonkglorg.utilitylib.item.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class HeadMenuGui extends InventoryGUI
{
	private final Heads plugin;
	
	public HeadMenuGui(Heads plugin, Profile profile)
	{
		super(9, "Head Gui", plugin, profile);
		this.plugin = plugin;
	}
	
	@Override
	public void addComponents()
	{
		fill(FILLER);
		addButton(HeadDropConfigButton(), 1);
		addButton(AdvancementsButton(), 3);
		addButton(CloseButton(), 7);
	}
	
	private Button HeadDropConfigButton()
	{
		ItemStack icon = ItemUtil.createPlayerHead(UUID.fromString("30ef44d4-f1bb-4e9f-b079-d5d62364c244"), "Head Config");
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				new HeadMenuPage(plugin,
						profile,
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
					new AdvancementMenuPage(plugin, profile, plugin.getAdvancementHandler()).open();
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
				profile.getOwner().closeInventory();
			}
		};
	}
}