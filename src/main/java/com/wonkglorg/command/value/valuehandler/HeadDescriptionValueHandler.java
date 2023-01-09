package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.command.value.ChangeValueCommand.DataChange;
import com.wonkglorg.heads.MobHeadData;

public class HeadDescriptionValueHandler implements HeadValueHandler
{
	
	@Override
	public void accept(MobHeadData mobHeadData, DataChange value)
	{
		mobHeadData.setDescription(value.stringValue());
	}
}