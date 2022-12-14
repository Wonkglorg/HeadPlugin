package com.wonkglorg.utils;

import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.MenuUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HeadMenuUtility extends MenuUtility
{
	private String lastPath;
	private MenuDataVariables dataVariables;
	private MobHeadData mobHeadData;
	private double incrementSize;
	private Button selectedButton;
	
	private int lastPage = 1;
	
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
	
	public String getLastPath()
	{
		return lastPath;
	}
	
	public void setLastPath(String lastPath)
	{
		this.lastPath = lastPath;
	}
	
	public int getLastPage()
	{
		return lastPage;
	}
	
	public void setLastPage(int lastPage)
	{
		this.lastPage = lastPage;
	}
}