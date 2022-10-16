package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.Utils;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MenuDataVariables;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class HeadConfigurationPage extends InventoryGUI
{
	private final MobHeadData headData;
	private final static List<Player> playerList = new ArrayList<>();
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	private final Player player;
	private boolean changes;
	
	public HeadConfigurationPage(HeadMenuUtility menuUtility, boolean changes)
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
		addButton(changeName(this), 19);
		addButton(changeDescription(this), 22);
		addButton(changeTexture(this), 25);
		if(changes)
		{
			addButton(accept(this), 45);
		}
		addButton(setEnabled((HeadMenuUtility) menuUtility, this), 40);
		addButton(dropChance((HeadMenuUtility) menuUtility, this), 43);
		//only add headbutton if changes have been made
		addButton(back((this)), 49);
	}
	
	private Button headShowcase(ItemStack itemStack)
	{
		return new Button(itemStack)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				Utils.give((Player) e.getWhoClicked(), getItem());
			}
		};
	}
	
	private Button accept(HeadConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.LIME_CONCRETE).setName("Confirm").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headMenuUtility = (HeadMenuUtility) menuUtility;
				headMenuUtility.getMobHeadData().setValues();
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
				new HeadConfigGui(headMenuUtility,
						Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),
						builder.toString());
				headMenuUtility.setMobHeadData(null);
			}
		};
	}
	
	private Button back(HeadConfigurationPage headConfigurationPage)
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
				new HeadConfigGui(headMenuUtility,
						Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()),
						builder.toString());
				headMenuUtility.setMobHeadData(null);
			}
		};
	}
	
	private Button setEnabled(HeadMenuUtility menuUtility, HeadConfigurationPage headConfigurationPage)
	{
		ItemStack icon;
		if(menuUtility.getMobHeadData().isEnabled())
		{
			icon = new ItemBuilder(Material.LIME_CONCRETE).setName("Enabled").build();
		} else
		{
			icon = new ItemBuilder(Material.RED_CONCRETE).setName("Disabled").build();
		}
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				menuUtility.getMobHeadData().setEnabled(!menuUtility.getMobHeadData().isEnabled());
				headConfigurationPage.destroy();
				new HeadConfigurationPage(menuUtility, true).open();
			}
		};
	}
	
	//ADD BUTTON FUNCTION TO SET % DROP RATE WITH CLICKS AND SHIFT CLICK
	//MAKE BUTTON CODE MORE EFFICIENT
	private Button changeName(HeadConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.OAK_SIGN).setName("Change Name").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headUtil = (HeadMenuUtility) menuUtility;
				HeadConfigurationPage.playerList.add(headUtil.getOwner());
				headUtil.setDataVariables(MenuDataVariables.NAME);
				headConfigurationPage.destroy();
				headConfigurationPage.getInventory().close();
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-name"));
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-cancel"));
			}
		};
	}
	
	private Button changeDescription(HeadConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.OAK_SAPLING).setName("Change Description").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headUtil = (HeadMenuUtility) menuUtility;
				HeadConfigurationPage.playerList.add(headUtil.getOwner());
				headUtil.setDataVariables(MenuDataVariables.DESCRIPTION);
				headConfigurationPage.destroy();
				headConfigurationPage.getInventory().close();
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-description"));
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-cancel"));
			}
		};
	}
	
	private Button changeTexture(HeadConfigurationPage headConfigurationPage)
	{
		ItemStack icon = new ItemBuilder(Material.BEACON).setName("Change Texture").build();
		return new Button(new ItemStack(icon))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				HeadMenuUtility headUtil = (HeadMenuUtility) menuUtility;
				HeadConfigurationPage.playerList.add(headUtil.getOwner());
				headUtil.setDataVariables(MenuDataVariables.TEXTURE);
				headConfigurationPage.destroy();
				headConfigurationPage.getInventory().close();
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-change-texture"));
				Message.msgPlayer(menuUtility.getOwner(), lang.getValue(player, "command-request-cancel"));
			}
		};
	}
	
	private Button dropChance(HeadMenuUtility menuUtility, HeadConfigurationPage headConfigurationPage)
	{
		double dropchance = menuUtility.getMobHeadData().getDropChance();
		Color color = ChatColor.gradient(100, 0, dropchance, Color.GREEN, Color.RED);
		String buf = Integer.toHexString(color.getRGB());
		String hex = "#" + buf.substring(buf.length() - 6);
		ItemStack icon = new ItemBuilder(Material.LIGHT).setName("Dropchance").addLoreLine(ChatColor.HexColor(hex) + dropchance + "%").addLoreLine(
				ChatColor.Reset + ChatColor.GOLD + "Increment size: " + menuUtility.getIncrementSize()).addLoreLine(ChatColor.Reset + ChatColor.LIGHT_PURPLE + "Drop > Increment").addLoreLine(
				ChatColor.Reset + ChatColor.LIGHT_PURPLE +"CRTL + Drop > Decrement").build();
		
		//make an increment that can be changed by 0.1, 1, or 10
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				MobHeadData mobHeadData = menuUtility.getMobHeadData();
				
				switch(e.getClick())
				{
					case LEFT ->
					{
						double dropchance = mobHeadData.getDropChance() + menuUtility.getIncrementSize();
						double nearest = round(dropchance,1);
						mobHeadData.setDropChance(nearest > 100 ? 100 : nearest);
					}
					case RIGHT ->
					{
						double dropchance = mobHeadData.getDropChance() - menuUtility.getIncrementSize();
						double nearest = round(dropchance,1);
						mobHeadData.setDropChance(nearest < 0 ? 0 : nearest);
						
					}
					case DROP -> menuUtility.increment();
					case CONTROL_DROP -> menuUtility.decrement();
				}
				headConfigurationPage.update();
				new HeadConfigurationPage(menuUtility, true).open();
			}
		};
	}
	
	private double round (double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
	public static boolean exists(Player player)
	{
		return playerList.contains(player);
	}
	
	public static void removeEntry(Player player)
	{
		playerList.remove(player);
	}
	
}