package com.wonkglorg.gui.advancement;

import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.inventory.InventorySize;
import com.wonkglorg.utils.HeadProfile;
import org.bukkit.plugin.java.JavaPlugin;

public class Subvalues extends InventoryGUI
{
	private HeadProfile headProfile;
	public Subvalues(String name, JavaPlugin plugin, HeadProfile menuUtility)
	{
		super(InventorySize.MEDIUM, name, plugin, menuUtility);
		this.headProfile = menuUtility;
	}
	
	@Override
	public void addComponents()
	{
		/*
		switch(headProfile.getAdvancementEditValue()){
			case FRAME -> ;
			case VISIBILITY ->
		}
		
		 */
	}
	
	

	
}