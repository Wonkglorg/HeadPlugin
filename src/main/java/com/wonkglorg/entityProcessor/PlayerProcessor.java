package com.wonkglorg.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerProcessor extends EntityTypeProcessor
{
	@Override
	String path()
	{
		return null;
	}
	
	@Override
	public boolean matches(Entity entity)
	{
		return entity instanceof Player;
	}
	
	@Override
	public void process(Entity entity, Location loc)
	{
		if(entity instanceof Player player)
		{
			if(Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()).getBoolean("Heads.player.Enabled"))
			{
				player.getWorld().dropItemNaturally(player.getLocation(), ItemUtility.createPlayerHead(player.getUniqueId()));
			}
		}
	}
}