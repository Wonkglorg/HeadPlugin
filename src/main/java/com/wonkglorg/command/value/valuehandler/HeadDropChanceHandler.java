package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.manager.config.Config;
import org.bukkit.entity.Player;

public class HeadDropChanceHandler extends HeadValueHandler
{
	
	@Override
	public void accept(Player player, Config config, MobHeadData mobHeadData, String value)
	{
		double doubleValue;
		try
		{
			doubleValue = Double.parseDouble(value);
		}catch(Exception ignored){
			doubleValue = 0;
		}
		
		mobHeadData.setDropChance(doubleValue >= 0 && doubleValue <= 100 ? round(doubleValue, 2) : mobHeadData.getDropChance());
		
		handle(player, config, mobHeadData);
	}
	
	//MOVE TO MENU UTILITY MATH OPERATIONS
	private double round(double value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
}