package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.MenuUtility;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HeadConfig extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public HeadConfig(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		HeadMenuUtility menuUtility = HeadMenuUtility.get(player);
		InventoryGUI inventoryGUI = new InventoryGUI(54,"Head Config",plugin,menuUtility){
			@Override
			public void addComponents()
			{
			
			}
		};
		new HeadConfigGui(inventoryGUI,menuUtility,Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()));
		
		//try to understand pagination menu
		return true;
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
}