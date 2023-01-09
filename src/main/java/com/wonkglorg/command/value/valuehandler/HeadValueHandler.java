package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.command.value.ChangeValueCommand.DataChange;
import com.wonkglorg.heads.MobHeadData;

public  interface HeadValueHandler
{
	void accept(MobHeadData mobHeadData, DataChange value);
}