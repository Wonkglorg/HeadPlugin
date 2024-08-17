package com.wonkglorg.heads.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.base.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

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
			if(Heads.manager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()).getBoolean("Heads.player.Enabled"))
			{
				player.getWorld().dropItemNaturally(player.getLocation(), ItemUtil.createPlayerHead(player.getUniqueId()));
				player.getPersistentDataContainer().set(new NamespacedKey(Heads.getInstance(), "drophead"), PersistentDataType.STRING, "false");
			}
		}
	}
}