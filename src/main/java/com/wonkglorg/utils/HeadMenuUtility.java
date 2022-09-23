package com.wonkglorg.utils;

import com.wonkglorg.MobHeadData;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.MenuUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HeadMenuUtility extends MenuUtility
{
	private InventoryGUI lastInventory;
	private MobHeadData mobHeadData;
	/**
	 * Instantiates a new Menu utility.
	 *
	 * @param player the player
	 */
	public HeadMenuUtility(Player player)
	{
		super(player);
	}
	
	/**
	 * Loads the MenuUtility for the specified {@link Player} or creates a new one if there is non.
	 *
	 * @param player {@link Player} opening the menu.
	 * @return MenuUtility linked to the {@link Player}.
	 */
	public static HeadMenuUtility get(@NotNull Player player)
	{
		HeadMenuUtility playerMenuUtility;
		if(!(getMenuUtilityMap().containsKey(player)))
		{
			playerMenuUtility = new HeadMenuUtility(player);
			addEntry(player,playerMenuUtility);
			
			return playerMenuUtility;
		}
			return (HeadMenuUtility) getMenuUtilityMap().get(player);
	}
	
	public MobHeadData getMobHeadData()
	{
		return mobHeadData;
	}
	
	public void setMobHeadData(MobHeadData mobHeadData)
	{
		this.mobHeadData = mobHeadData;
	}
	
	public InventoryGUI getLastInventory()
	{
		return lastInventory;
	}
	
	public void setLastInventory(InventoryGUI lastInventory)
	{
		this.lastInventory = lastInventory;
	}
}