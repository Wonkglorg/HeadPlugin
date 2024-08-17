package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.Heads;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.heads.MobHeadDataUtility;
import com.wonkglorg.utilitylib.base.message.Message;
import com.wonkglorg.utilitylib.manager.config.Config;
import com.wonkglorg.utils.HeadProfile;
import org.bukkit.entity.Player;

public class HeadFileValueHandler extends HeadValueHandler
{
	@Override
	public void accept(Player player, Config config, MobHeadData mobHeadData, String value)
	{
		HeadProfile menuUtility = Heads.getProfileManager().get(player);
		String path = menuUtility.getLastPath() + "." + value;
		System.out.println(path);
		if(MobHeadData.isValidHeadPath(config, path))
		{
			Message.msgPlayer(player, Heads.manager().getLangManager().getValue(player, "command-value-error-value-exists"));
			return;
		}
		MobHeadDataUtility.createNewDirectory(config, menuUtility.getLastPath(), value);
	}
}