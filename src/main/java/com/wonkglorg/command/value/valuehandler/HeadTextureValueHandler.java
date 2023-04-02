package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.config.Config;
import org.bukkit.entity.Player;

public class HeadTextureValueHandler extends HeadValueHandler
{
	
	
	@Override
	public void accept(Player player, Config config, MobHeadData mobHeadData, String value)
	{
		mobHeadData.setTexture(value);
		
		handle(player, config, mobHeadData);
	}
}