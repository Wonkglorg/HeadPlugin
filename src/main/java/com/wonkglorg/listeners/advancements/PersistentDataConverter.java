package com.wonkglorg.listeners.advancements;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PersistentDataConverter implements Listener
{
	@EventHandler
	public void onHeadPlace(BlockPlaceEvent e)
	{
		e.getBlockPlaced().getLocation();
		//get the data container and add it to placed head
	}
	
	@EventHandler
	public void onHeadMine(BlockBreakEvent e)
	{
		
		//get the data container from placed head and add it to the item stack
	}
}