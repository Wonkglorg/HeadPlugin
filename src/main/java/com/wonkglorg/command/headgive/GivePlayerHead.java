package com.wonkglorg.command.headgive;

import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.utils.Utils;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class GivePlayerHead extends Command
{
	/**
	 * Instantiates a new Command.
	 *
	 * @param plugin the plugin
	 * @param name the name
	 */
	public GivePlayerHead(@NotNull JavaPlugin plugin, @NotNull String name)
	{
		super(plugin, name);
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		OfflinePlayer playerName;
		String name;
		String description;
		if(args.length < 1)
		{
			return false;
		}
		
		if(args.length == 1)
		{
			playerName = argAsOfflinePlayer(0, Bukkit.getOfflinePlayer(UUID.fromString("30ef44d4f1bb4e9fb079d5d62364c244")));
			Utils.give(player, ItemUtility.createPlayerHead(playerName.getUniqueId()));
			return true;
		}
		if(args.length == 2)
		{
			playerName = argAsOfflinePlayer(0, Bukkit.getOfflinePlayer(UUID.fromString("30ef44d4f1bb4e9fb079d5d62364c244")));
			name = argAsString(1);
			Utils.give(player, ItemUtility.setName(ItemUtility.createPlayerHead(playerName.getUniqueId()), name));
			return true;
		}
		
		playerName = argAsOfflinePlayer(0, Bukkit.getOfflinePlayer(UUID.fromString("30ef44d4f1bb4e9fb079d5d62364c244")));
		name = argAsString(1);
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 2; args.length > i; i++)
		{
			stringBuilder.append(argAsString(i));
		}
		description = stringBuilder.toString();
		ItemStack head = ItemUtility.createPlayerHead(playerName.getUniqueId());
		head = ItemUtility.setName(head,name);
		ItemUtility.addLore(head,description);
		Utils.give(player,head);
		return true;
	}
	
	private void addHead(Player pLayer,UUID uuid,String name, String... description){
		
		
		
		ItemUtility.give(ItemUtility.createPlayerHead());
	}
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		return null;
	}
}