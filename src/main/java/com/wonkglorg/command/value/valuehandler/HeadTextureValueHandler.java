package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.command.value.ChangeValueCommand.DataChange;
import com.wonkglorg.heads.MobHeadData;

public class HeadTextureValueHandler implements HeadValueHandler
{
	
	@Override
	public void accept(MobHeadData mobHeadData, DataChange value)
	{
		mobHeadData.setTexture(value.stringValue());
	}
}