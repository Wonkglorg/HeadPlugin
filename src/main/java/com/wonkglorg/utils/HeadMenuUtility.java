package com.wonkglorg.utils;

import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.MenuUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HeadMenuUtility extends MenuUtility
{
	private String lastPath;
	private MenuDataVariables dataVariables;
	private MobHeadData mobHeadData;
	private double incrementSize;
	private Button selectedButton;
	
	/**
	 * Instantiates a new Menu utility.
	 *
	 * @param player the player
	 */
	public HeadMenuUtility(Player player)
	{
		super(player);
		incrementSize = 1;
	}
	
	/**
	 * Loads the MenuUtility for the specified {@link Player} or creates a new one if there is non.
	 *
	 * @param player {@link Player} opening the menu.
	 * @return MenuUtility linked to the {@link Player}.
	 */
	public static HeadMenuUtility get(@NotNull Player player)
	{
		//ADD GETTER METHOD TO SUPER CLASS TO EASIER IMPLEMENT
		HeadMenuUtility playerMenuUtility;
		if(!(getMenuUtilityMap().containsKey(player)))
		{
			playerMenuUtility = new HeadMenuUtility(player);
			addEntry(player, playerMenuUtility);
			
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
	
	public MenuDataVariables getDataVariable()
	{
		return dataVariables;
	}
	
	public void setDataVariables(MenuDataVariables dataVariables)
	{
		this.dataVariables = dataVariables;
	}
	
	public void increment()
	{
		if(incrementSize == 0.1)
		{
			incrementSize = 1;
			return;
		}
		if(incrementSize == 1)
		{
			incrementSize = 10;
		}
	}
	
	public Button getSelectedButton()
	{
		return selectedButton;
	}
	
	public void setSelectedButton(Button selectedButton)
	{
		this.selectedButton = selectedButton;
	}
	
	public void decrement()
	{
		if(incrementSize == 10)
		{
			incrementSize = 1;
			return;
		}
		if(incrementSize == 1)
		{
			incrementSize = 0.1;
		}
	}
	
	public double getIncrementSize()
	{
		return incrementSize;
	}
	
	public String getLastPath()
	{
		return lastPath;
	}
	
	public void setLastPath(String lastPath)
	{
		this.lastPath = lastPath;
	}
	
	public void setIncrementSize(double incrementSize)
	{
		this.incrementSize = incrementSize;
	}
}