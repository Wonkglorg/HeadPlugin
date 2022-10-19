package com.wonkglorg;

import com.wonkglorg.command.config.ReloadConfigs;
import com.wonkglorg.command.config_gui.ChangeValueCommand;
import com.wonkglorg.command.config_gui.HeadConfigCommand;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.command.dropchance.DropChance;
import com.wonkglorg.command.headgive.GiveCustomHead;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.utilitylib.config.ConfigYML;
import com.wonkglorg.utilitylib.managers.PluginManager;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class Heads extends JavaPlugin
{
	private static PluginManager pluginManager;
	private static Heads heads;
	
	@Override
	public void onEnable()
	{
		heads = this;
		pluginManager = new PluginManager(this);
		addConfigs();
		addListeners();
		addCommands();
	}
	
	@Override
	public void onDisable()
	{
		pluginManager.getConfigManager().save();
	}
	
	private void addCommands()
	{
		new ChargedCreeper(this, "spawn-creeper");
		
		new GiveCustomHead(this, "givecustomhead");
		new GiveMobHeadCommand(this, "givemobhead");
		
		new ReloadConfigs(this, "head-reload", pluginManager.getConfigManager());
		
		new HeadConfigCommand(this, "headconfig");
		new ChangeValueCommand(this, "value");
		
		new DropChance(this,"dropchance");
	}
	
	private void addListeners()
	{
		new DamageListener(this);
		new DeathListener(this);
		new ClickListener(this);
		new ExplosionEvent(this);
	}
	
	private void addConfigs()
	{
		pluginManager.addConfig(new ConfigYML(this, YML.CONFIG.getFileName()));
		pluginManager.addConfig(new ConfigYML(this, YML.HEAD_DATA.getFileName()));
		pluginManager.addConfig(new ConfigYML(this, YML.HEAD_DROP_NUMBERS.getFileName()));
		pluginManager.addConfig(new ConfigYML(this, YML.HEAD_DATA_BACKUP.getFileName()));
		
		pluginManager.setDefaultLang(Locale.ENGLISH, new ConfigYML(this, "eng.yml", "lang"));
		pluginManager.getLangManager().replace("<prefix>", ChatColor.GRAY + "[HeadPlugin]&r");
		pluginManager.registerAll();
	}
	
	public static Heads getInstance()
	{
		return heads;
	}
	
	public static PluginManager getPluginManager()
	{
		return pluginManager;
	}
	
}
