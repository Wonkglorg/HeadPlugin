package com.wonkglorg.listeners;

import com.wonkglorg.utils.MobHeadData;
import com.wonkglorg.command.config_gui.HeadConfigurationPage;
import com.wonkglorg.utilitylib.listener.EventListener;
import com.wonkglorg.utils.HeadMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatEvent extends EventListener
{
	public ChatEvent(JavaPlugin plugin)
	{
		super(plugin);
	}
	
	
	//replace with async chat event
	@EventHandler(priority = EventPriority.HIGH)
	public void chatMessage(PlayerChatEvent e)
	{
		if(!HeadConfigurationPage.exists(e.getPlayer()))
		{
			return;
		}
		
		Player player = e.getPlayer();
		String message = e.getMessage();
		HeadMenuUtility headMenuUtility = HeadMenuUtility.get(e.getPlayer());
		MobHeadData mobHeadData = headMenuUtility.getMobHeadData();
		
		if(message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("quit")){
			e.setCancelled(true);
			new HeadConfigurationPage(headMenuUtility,false).open();
			HeadConfigurationPage.removeEntry(player);
		}
		
		switch(headMenuUtility.getDataVariable())
		{
			case NAME -> mobHeadData.setName(message);
			case TEXTURE -> mobHeadData.setTexture(message);
			case DESCRIPTION -> mobHeadData.setDescription(message);
		}
		e.setCancelled(true);
		headMenuUtility.setMobHeadData(mobHeadData);
		new HeadConfigurationPage(headMenuUtility,true).open();
		HeadConfigurationPage.removeEntry(player);
	}
}