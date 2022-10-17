package com.wonkglorg.listeners;

import com.wonkglorg.Heads;
import com.wonkglorg.command.config_gui.HeadConfigGui;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utils.MenuDataVariables;
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
	
	private Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
	//replace with async chat event
	@EventHandler(priority = EventPriority.HIGH)
	public void chatMessage(PlayerChatEvent e)
	{
		if(!Heads.exists(e.getPlayer()))
		{
			return;
		}
		
		Player player = e.getPlayer();
		String message = e.getMessage();
		HeadMenuUtility headMenuUtility = HeadMenuUtility.get(e.getPlayer());
		MobHeadData mobHeadData = headMenuUtility.getMobHeadData();
		
		if(message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("quit")){
			e.setCancelled(true);
			if(headMenuUtility.getDataVariable() == MenuDataVariables.FILENAME){
				new HeadConfigGui(headMenuUtility,config, headMenuUtility.getLastPath());
				Heads.remove(player);
				return;
			}
			new HeadConfigurationPage(headMenuUtility,false).open();
			Heads.remove(player);
		}
		
		switch(headMenuUtility.getDataVariable())
		{
			case NAME -> mobHeadData.setName(message);
			case TEXTURE -> mobHeadData.setTexture(message);
			case DESCRIPTION -> mobHeadData.setDescription(message);
			case FILENAME ->  {
				e.setCancelled(true);
				MobHeadData.createNewDirectory(config,headMenuUtility.getLastPath(),message);
				new HeadConfigGui(headMenuUtility,config, headMenuUtility.getLastPath());
				return;
			}
			
		}
		e.setCancelled(true);
		headMenuUtility.setMobHeadData(mobHeadData);
		new HeadConfigurationPage(headMenuUtility,true).open();
		Heads.remove(player);
	}
}