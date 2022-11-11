package com.wonkglorg;

import com.wonkglorg.command.config.ReloadConfigs;
import com.wonkglorg.command.config_gui.ChangeValueCommand;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.command.dropchance.DropChance;
import com.wonkglorg.command.headgive.GiveCustomHead;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.utilitylib.UtilityPlugin;
import com.wonkglorg.utilitylib.config.ConfigYML;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class Heads extends UtilityPlugin
{
	private static Heads plugin;
	
	@Override
	public void pluginStartup()
	{
		plugin = this;
	}
	
	@Override
	public void pluginShutdown()
	{
	
	}
	
	@Override
	public void event()
	{
		manager.add(new DamageListener(this));
		manager.add(new DeathListener(this));
		manager.add(new ClickListener(this));
		manager.add(new ExplosionEvent(this));
	}
	
	@Override
	public void command()
	{
		manager.add(new ChargedCreeper(this, "spawn-creeper"));
		manager.add(new GiveCustomHead(this, "givecustomhead"));
		manager.add(new GiveMobHeadCommand(this, "givemobhead"));
		manager.add(new ReloadConfigs(this, "head-config-reload", manager.getConfigManager()));
		manager.add(new ChangeValueCommand(this, "value"));
		manager.add(new DropChance(this, "dropchance"));
	}
	
	@Override
	public void config()
	{
		manager.add(new ConfigYML(this, YML.CONFIG.getFileName()));
		manager.add(new ConfigYML(this, YML.HEAD_DATA.getFileName()));
		manager.add(new ConfigYML(this, YML.HEAD_DROP_NUMBERS.getFileName()));
		manager.add(new ConfigYML(this, YML.HEAD_DATA_BACKUP.getFileName()));
	}
	
	@Override
	public void lang()
	{
		manager.getLangManager().setDefaultLang(Locale.ENGLISH, new ConfigYML(this, "eng.yml", "lang"));
		manager.getLangManager().replace("<prefix>", ChatColor.GRAY + "[HeadPlugin]&r");
	}
	
	@Override
	public void recipe()
	{
	
	}
	
	@Override
	public void enchant()
	{
	
	}
	
	public static JavaPlugin getInstance()
	{
		return plugin;
	}

}
