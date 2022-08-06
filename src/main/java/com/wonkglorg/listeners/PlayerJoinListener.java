package com.wonkglorg.listeners;

import com.wonkglorg.utilitylib.utils.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoinListener implements Listener
{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Bukkit.broadcast(Message.color("Single Test Message"));
		Bukkit.broadcast(Message.color("&#101010 Single Test Message Colored"));
		for(Component component : Message.color(List.of("&b This is Bold", "&i This is Italic", "&#101010 This is colored","&r This is Default"))){
			Bukkit.broadcast(component);
		}
	}
	
}