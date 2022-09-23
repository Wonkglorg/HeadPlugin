package com.wonkglorg;

import com.wonkglorg.command.Editor;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.command.ReloadConfigs;
import com.wonkglorg.command.config_gui.HeadConfig;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.listeners.HeadPickupListener;
import com.wonkglorg.listeners.Milk;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Heads extends JavaPlugin
{
	private static PluginManager pluginManager;
	private static Heads heads;
	
	@Override
	public void onEnable()
	{
		// ADD METHOD TO SEE DROP CHANCES AND MAKE GUI TO GO THROUGH ALL HEADS THAT EXIST AND ADD INFORMATION LIKE DROP CHANCE, SORT TEHM BY BELONGING TOGETHER
		//Configs
		heads = this;
		pluginManager = new PluginManager(this);
		addConfigs();
		addListeners();
		addCommands();
		
	}
	
	@Override
	public void onDisable()
	{
	}
	
	private void addCommands()
	{
		new ChargedCreeper(this, "spawn-creeper");
		new GiveMobHeadCommand(this, "give-mob-head");
		new
		new ReloadConfigs(this, "reload-heads", pluginManager.getConfigManager());
		new Editor(this,"head-editor");
		new HeadConfig(this,"headconfig");
	}
	
	private void addListeners()
	{
		new DamageListener(this);
		new DeathListener(this);
		new ClickListener(this);
		new ExplosionEvent(this);
		new HeadPickupListener(this);
		new Milk(this);
	}
	
	private void addConfigs()
	{
		pluginManager.addConfig(new Config(this, YML.CONFIG.getFileName()));
		pluginManager.addConfig(new Config(this, YML.HEAD_DATA.getFileName()));
		pluginManager.addConfig(new Config(this, YML.ENGLISH.getFileName()));
		pluginManager.registerAll();
	}
	public static Heads getInstance(){
		return heads;
	}
	public static PluginManager getPluginManager()
	{
		return pluginManager;
	}
}
