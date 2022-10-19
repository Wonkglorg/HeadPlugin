package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.Utils;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.Color;
import java.util.Arrays;

public class ConfigurationPage extends InventoryGUI
{
	private final MobHeadData headData;
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	private final Config backupConfig = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA_BACKUP.getFileName());
	private final Player player;
	private final boolean changes;
	private boolean resetConfirmed = false;
	
	public ConfigurationPage(HeadMenuUtility menuUtility, boolean changes)
	{
		super(54, menuUtility.getMobHeadData().getName(), Heads.getInstance(), menuUtility);
		player = menuUtility.getOwner();
		this.changes = changes;
		headData = menuUtility.getMobHeadData();
	}
	
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
		String[] path = headData.getPath().split("\\.");
		StringBuilder builder = new StringBuilder();
		for(String s : Arrays.copyOf(path, path.length - 1))
		{
			builder.append(s).append(".");
		}
		
		if(MobHeadData.isValidHeadPath(backupConfig, builder.toString().trim() + "default"))
		{
			addButton(resetHeadToDefault(headData.getPath(), builder.toString().trim() + "default"), 41);
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
				Utils.give((Player) e.getWhoClicked(), headData.createHeadItem());
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
				String[] path = headMenuUtility.getMobHeadData().getPath().split("\\.");
				StringBuilder builder = new StringBuilder();
				builder.append(path[0]);
				for(int i = 1; i < path.length - 1; i++)
				{
					builder.append(".");
					builder.append(path[i]);
				}
				new MenuPage(headMenuUtility,
						Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),
						builder.toString(),
						null);
				headMenuUtility.setMobHeadData(null);
				destroy();
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
				String[] path = headMenuUtility.getMobHeadData().getPath().split("\\.");
				headConfigurationPage.destroy();
				StringBuilder builder = new StringBuilder();
				builder.append(path[0]);
				for(int i = 1; i < path.length - 1; i++)
				{
					builder.append(".");
					builder.append(path[i]);
				}
				new MenuPage(headMenuUtility,
						Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),
						builder.toString(),
						null);
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
		
		//NAME DOESN'T MATTER GET DEFAULT VALUE FROM PATH YOU ARE IN NO MATTER THE HEAD
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
					new ConfigurationPage((HeadMenuUtility) menuUtility, false).open();
					update();
					resetConfirmed = false;
					return;
				}
				resetConfirmed = true;
				setItem(ItemUtility.rename(icon, "Confirm reset?"));
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
				
				switch(e.getClick())
				{
					case LEFT ->
					{
						
						ChangeValueCommand.setPlayerDataChange(player, mobHeadData);
						handleChange(MenuDataVariables.DROPCHANCE);
						destroy();
						update();
						getInventory().close();
						/*
						double dropchance = mobHeadData.getDropChance() + menuUtility.getIncrementSize();
						double nearest = round(dropchance, 1);
						mobHeadData.setDropChance(nearest > 100 ? 100 : nearest);
						
						 */
					}
					/*
					case RIGHT ->
					{
						double dropchance = mobHeadData.getDropChance() - menuUtility.getIncrementSize();
						double nearest = round(dropchance, 1);
						mobHeadData.setDropChance(nearest < 0 ? 0 : nearest);
						
					}
					case DROP -> menuUtility.increment();
					case CONTROL_DROP -> menuUtility.decrement();
					case SHIFT_LEFT ->
					{
						
						ChangeValueCommand.setPlayerDataChange(player, mobHeadData);
						handleChange(MenuDataVariables.DROPCHANCE);
						destroy();
						update();
						getInventory().close();
					}
					
					 */
				}
				setItem(getChanceItemStack(menuUtility));
				addButton(accept(configurationPage), 45);
				update();
			}
		};
	}
	
	//Add reset button to reset back to
	
	private ItemStack getChanceItemStack(HeadMenuUtility menuUtility)
	{
		double dropchance = menuUtility.getMobHeadData().getDropChance();
		Color color = ChatColor.gradient(100, 0, dropchance, Color.GREEN, Color.RED);
		String buf = Integer.toHexString(color.getRGB());
		String hex = "#" + buf.substring(buf.length() - 6);
		return new ItemBuilder(Material.LIGHT).setName("Dropchance")
											  .addLoreLine(ChatColor.HexColor(hex) + dropchance + "%")
											  //.addLoreLine(ChatColor.Reset + ChatColor.GOLD + "Increment size: " + menuUtility.getIncrementSize())
											  //.addLoreLine(ChatColor.Reset + ChatColor.LIGHT_PURPLE + "Drop > Increment")
											  //.addLoreLine(ChatColor.Reset + ChatColor.LIGHT_PURPLE + "CRTL + Drop > Decrement")
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