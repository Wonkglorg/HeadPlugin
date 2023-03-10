package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.Heads;
import com.wonkglorg.command.value.ChangeValueCommand.DataChange;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.heads.MobHeadDataUtility;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.message.Message;
import com.wonkglorg.utils.HeadProfile;
import org.bukkit.entity.Player;

public class HeadFileValueHandler implements HeadValueHandler
{
	@Override
	public void accept(MobHeadData mobHeadData, DataChange value)
	{
		HeadProfile menuUtility = value.menuUtility();
		Player player = menuUtility.getOwner();
		String path = menuUtility.getLastPath() + "." + value.stringValue();
		System.out.println(path);
		Config config = Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
		if(MobHeadData.isValidHeadPath(config, path))
		{
			Message.msgPlayer(player, value.lang().getValue(player, "command-value-error-value-exists"));
			return;
		}
		MobHeadDataUtility.createNewDirectory(config, menuUtility.getLastPath(), value.stringValue());
		System.out.println("created new path");
	}
}