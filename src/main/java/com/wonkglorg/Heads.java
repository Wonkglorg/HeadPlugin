package com.wonkglorg;

import com.wonkglorg.command.HeadsCommand;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.config.ConfigManager;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.utilitylib.abstraction.Config;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Heads extends JavaPlugin
{
	static ArrayList<Entity> DropHeads = new ArrayList<>();
	private static Heads plugin;
	
	public static ConfigManager manager;
	public static ConfigManager langManager;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
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
	private void addCommands(){
		new ChargedCreeper(this,"creeperSpawner");
		new HeadsCommand(this,"drop-heads");
	}
	private void addListeners(){
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new DamageListener(), this);
		pluginManager.registerEvents(new DeathListener(), this);
		pluginManager.registerEvents(new ClickListener(),this);
		pluginManager.registerEvents(new ExplosionEvent(),this);
	}
	private void addConfigs()
	{
		
		manager = new ConfigManager();
		manager.addConfig(new Config(this,YML.CONFIG.getFileName()));
		manager.addConfig(new Config(this, YML.HEAD_DATA.getFileName()));
		manager.loadConfigs();
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
