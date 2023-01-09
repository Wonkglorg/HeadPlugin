package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.command.value.ChangeValueCommand.DataChange;
import com.wonkglorg.heads.MobHeadData;

public class HeadDropChanceHandler implements HeadValueHandler
{
	@Override
	public void accept(MobHeadData mobHeadData, DataChange value)
	{
		double doubleValue = value.doubleValue();
		mobHeadData.setDropChance(doubleValue >= 0 && doubleValue <= 100 ? round(doubleValue, 2) : mobHeadData.getDropChance());
	}
	
	
	//MOVE TO MENU UTILITY MATH OPERATIONS
	private double round(double value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
}