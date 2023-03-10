package com.wonkglorg.utils;

import com.wonkglorg.advancements.AdvancementData;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.Profile;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import org.bukkit.entity.Player;

public class HeadProfile extends Profile
{
	private String lastPath;
	private MenuDataVariables dataVariables;
	private MobHeadData mobHeadData;
	private Button selectedButton;
	
	private AdvancementData advancementData;
	
	private AdvancementEditValue advancementEditValue;
	
	private int currentPage = 1;
	
	/**
	 * Instantiates a new Menu utility.
	 *
	 * @param player the player
	 */
	public HeadProfile(Player player)
	{
		super(player);
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
	
	public int getCurrentPage()
	{
		return currentPage;
	}
	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}
	
	public AdvancementData getAdvancementData()
	{
		return advancementData;
	}
	
	public void setAdvancementData(AdvancementData advancementData)
	{
		this.advancementData = advancementData;
	}
	
	public AdvancementEditValue getAdvancementEditValue()
	{
		return advancementEditValue;
	}
	
	
	
	public void setAdvancementEditValue(AdvancementEditValue advancementEditValue)
	{
		this.advancementEditValue = advancementEditValue;
	}
	
	public enum AdvancementEditValue
	{
		FRAME(),
		VISIBILITY(),
		
	}
}