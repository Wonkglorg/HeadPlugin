package com.wonkglorg;

import com.wonkglorg.command.HeadsCommand;
import com.wonkglorg.command.ReloadConfigs;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.config.ConfigManager;
import com.wonkglorg.utilitylib.lang.LangManager;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Heads extends JavaPlugin
{
	static ArrayList<Entity> DropHeads = new ArrayList<>();
	
	private static ConfigManager manager;
	private static LangManager langManager;
	
	@Override
	public void onEnable()
	{
		
		// ADD METHOD TO SEE DROP CHANCES AND MAKE GUI TO GO THROUGH ALL HEADS THAT EXIST AND ADD INFORMATION LIKE DROP CHANCE, SORT TEHM BY BELONGING TOGETHER
		//Configs
		addConfigs();
		addListeners();
		addCommands();
		
	}
	
	@Override
	public void onDisable()
	{
		DropHeads.clear();
	}
	
	private void addCommands()
	{
		new ChargedCreeper(this, "spawn-creeper");
		new HeadsCommand(this, "drop-heads");
		new ReloadConfigs(this, "reload-heads", manager);
	}
	
	private void addListeners()
	{
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new DamageListener(), this);
		pluginManager.registerEvents(new DeathListener(), this);
		pluginManager.registerEvents(new ClickListener(), this);
		pluginManager.registerEvents(new ExplosionEvent(), this);
	}
	
	private void addConfigs()
	{
		langManager = new LangManager();
		
		manager = new ConfigManager();
		manager.add(new Config(this, YML.CONFIG.getFileName()));
		manager.add(new Config(this, YML.HEAD_DATA.getFileName()));
		manager.add(new Config(this,YML.ENGLISH.getFileName()));
		manager.load();
	}
	
	public static void setArray(Entity entity)
	{
		DropHeads.add(entity);
	}
	
	public static ArrayList<Entity> getArray()
	{
		return DropHeads;
	}
	
	public static ConfigManager getManager()
	{
		return manager;
	}
}
