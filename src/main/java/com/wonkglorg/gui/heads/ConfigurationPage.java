package com.wonkglorg.gui.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.command.value.ChangeValueCommand;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.builder.ItemBuilder;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.item.ItemUtil;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.message.ChatColor;
import com.wonkglorg.utilitylib.message.Message;
import com.wonkglorg.utilitylib.utils.players.PlayerUtil;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.Color;

public class ConfigurationPage extends InventoryGUI
{
	private final Heads plugin;
	private final MobHeadData headData;
	private final LangManager lang = Heads.getManager().getLangManager();
	private final Config backupConfig = Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA_BACKUP.getFileName());
	private final Player player;
	private final Config config = Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
	private final boolean changes;
	private boolean resetConfirmed = false;
	
	public ConfigurationPage(Heads plugin, HeadMenuUtility menuUtility, boolean changes)
	{
		super(54, menuUtility.getMobHeadData().getName(), Heads.getPlugin(Heads.class), menuUtility);
		player = menuUtility.getOwner();
		this.changes = changes;
		this.plugin = plugin;
		headData = menuUtility.getMobHeadData();
	}
	
	// IMPLEMENT WAY TO ONLY ALLOW HEADS TO DROP FROM SPECIFIC SOURCE LIKE PLAYER KILL OR CREEPER
	@Override
	public void addComponents()
	{
		fill(0, super.getInventory().getSize(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
		addButton(headShowcase(headData.createHeadItemWithInfoDesc()), 4);
		addButton(changeName(), 19);
		addButton(changeDescription(), 22);
		addButton(changeTexture(), 25);
		addButton(setEnabled((HeadMenuUtility) menuUtility, this), 40);
		addButton(dropChance((HeadMenuUtility) menuUtility, this), 43);
		String defaultPath = config.getParentPath(headData.getPath());
		if(MobHeadData.isValidHeadPath(backupConfig, defaultPath + ".default"))
		{
			addButton(resetHeadToDefault(headData.getPath(), defaultPath + ".default"), 41);
		}
		if(changes)
		{
			addButton(accept(this), 45);
		}
		addButton(back((this)), 49);
	}
	
	private Button headShowcase(ItemStack itemStack)
	{
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				PlayerUtil.give((Player) e.getWhoClicked(), headData.createHeadItem());
			}
		};
	}
	
	private Button accept(ConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.LIME_CONCRETE).setName("Confirm").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headMenuUtility = (HeadMenuUtility) menuUtility;
				headMenuUtility.getMobHeadData().writeToConfig();
				Message.msgPlayer(menuUtility.getOwner(), "Successfully applied changes");
				headConfigurationPage.destroy();
				destroy();
				new HeadMenuPage(plugin, headMenuUtility, config, config.getParentPath(headMenuUtility.getMobHeadData().getPath()), null, 1).open();
				headMenuUtility.setMobHeadData(null);
			}
		};
	}
	
	private Button back(ConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.BARRIER).setName("Back").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headMenuUtility = (HeadMenuUtility) menuUtility;
				headConfigurationPage.destroy();
				new HeadMenuPage(plugin, headMenuUtility, config, config.getParentPath(headMenuUtility.getMobHeadData().getPath()), null, 1).open();
				headMenuUtility.setMobHeadData(null);
				destroy();
			}
		};
	}
	
	private Button setEnabled(HeadMenuUtility menuUtility, ConfigurationPage configurationPage)
	{
		ItemStack icon = menuUtility.getMobHeadData().isEnabled()
						 ? new ItemBuilder(Material.LIME_CONCRETE).setName("Enabled").build()
						 : new ItemBuilder(Material.RED_CONCRETE).setName("Disabled").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				menuUtility.getMobHeadData().setEnabled(!menuUtility.getMobHeadData().isEnabled());
				setItem(menuUtility.getMobHeadData().isEnabled()
						? new ItemBuilder(Material.LIME_CONCRETE).setName("Enabled").build()
						: new ItemBuilder(Material.RED_CONCRETE).setName("Disabled").build());
				addButton(accept(configurationPage), 45);
				update();
			}
		};
	}
	
	private Button changeName()
	{
		ItemStack icon = new ItemBuilder(Material.OAK_SIGN).setName("Change Name").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				handleChange(MenuDataVariables.NAME);
				destroy();
				getInventory().close();
			}
		};
	}
	
	private Button changeDescription()
	{
		ItemStack icon = new ItemBuilder(Material.OAK_SAPLING).setName("Change Description").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				handleChange(MenuDataVariables.DESCRIPTION);
				destroy();
				getInventory().close();
			}
		};
	}
	
	private Button resetHeadToDefault(String path, String backupDefaultPath)
	{
		
		ItemStack icon = new ItemBuilder(Material.BEACON).setName("Reset to default values").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(resetConfirmed)
				{
					headData.setValuesFromHeadData(new MobHeadData(backupDefaultPath, backupConfig, 1));
					headData.setPath(path);
					headData.writeToConfig();
					setItem(icon);
					destroy();
					new ConfigurationPage(plugin, (HeadMenuUtility) menuUtility, false).open();
					update();
					resetConfirmed = false;
					return;
				}
				resetConfirmed = true;
				setItem(ItemUtil.rename(icon, "Confirm reset?"));
				update();
			}
		};
	}
	
	private Button changeTexture()
	{
		ItemStack icon = new ItemBuilder(Material.BEACON).setName("Change Texture").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				handleChange(MenuDataVariables.TEXTURE);
				destroy();
				getInventory().close();
			}
		};
	}
	
	private Button dropChance(HeadMenuUtility menuUtility, ConfigurationPage configurationPage)
	{
		return new Button(getChanceItemStack(menuUtility))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				MobHeadData mobHeadData = menuUtility.getMobHeadData();
				
				if(e.getClick() == ClickType.LEFT)
				{
					ChangeValueCommand.setPlayerDataChange(player, mobHeadData);
					handleChange(MenuDataVariables.DROPCHANCE);
					destroy();
					update();
					getInventory().close();
				}
				setItem(getChanceItemStack(menuUtility));
				addButton(accept(configurationPage), 45);
				update();
			}
		};
	}
	
	private ItemStack getChanceItemStack(HeadMenuUtility menuUtility)
	{
		double dropchance = menuUtility.getMobHeadData().getDropChance();
		Color color = ChatColor.gradient(100, 0, dropchance, Color.GREEN, Color.RED);
		String buf = Integer.toHexString(color.getRGB());
		String hex = "#" + buf.substring(buf.length() - 6);
		return new ItemBuilder(Material.LIGHT).setName("Dropchance")
											  .addLoreLine(ChatColor.HexColor(hex) + dropchance + "%")
											  .addLoreLine(ChatColor.RED + ChatColor.GREEN + "Left click to set value")
											  .build();
	}
	
	private void handleChange(MenuDataVariables menuDataVariables)
	{
		HeadMenuUtility headUtil = (HeadMenuUtility) menuUtility;
		ChangeValueCommand.setPlayerDataChange(headUtil.getOwner(), headData);
		headUtil.setDataVariables(menuDataVariables);
		switch(menuDataVariables)
		{
			case DESCRIPTION -> Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-description"));
			case TEXTURE -> Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-texture"));
			case NAME -> Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-name"));
			case DROPCHANCE -> Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-dropchance"));
		}
		Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-cancel"));
	}
	
}