package com.wonkglorg.command.value.valuehandler;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.gui.heads.ConfigurationPage;
import com.wonkglorg.gui.heads.HeadMenuPage;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utils.HeadProfile;
import org.bukkit.entity.Player;

public abstract class HeadValueHandler
{
	public abstract void accept(Player player, Config config, MobHeadData mobHeadData, String value);
	
	protected void handle(Player player, Config config, MobHeadData mobHeadData)
	{
		HeadProfile headProfile = Heads.getProfileManager().get(player);
		headProfile.setMobHeadData(mobHeadData);
		if(headProfile.getDataVariable() == MenuDataVariables.FILENAME)
		{
			new HeadMenuPage((Heads) Heads.getInstance(), headProfile, config, headProfile.getLastPath(), null, headProfile.getCurrentPage()).open();
			headProfile.setDataVariables(null);
			return;
		}
		headProfile.setDataVariables(null);
		new ConfigurationPage((Heads) Heads.getInstance(), headProfile, true).open();
	}
}