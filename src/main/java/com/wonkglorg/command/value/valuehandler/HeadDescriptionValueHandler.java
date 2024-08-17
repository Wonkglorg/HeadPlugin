package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.manager.config.Config;
import org.bukkit.entity.Player;

public class HeadDescriptionValueHandler extends HeadValueHandler
{
	
	@Override
	public void accept(Player player, Config config, MobHeadData mobHeadData, String value)
	{
		mobHeadData.setDescription(value);
		
		handle(player,config,mobHeadData);
	}
}