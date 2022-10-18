package com.wonkglorg;

import com.wonkglorg.command.ReloadConfigs;
import com.wonkglorg.command.config_gui.HeadConfig;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.command.headgive.GiveCustomHead;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.ChatEvent;
import com.wonkglorg.listeners.DamageListener;
import com.wonkglorg.listeners.DeathListener;
import com.wonkglorg.listeners.HeadPickupListener;
import com.wonkglorg.utilitylib.config.ConfigYML;
import com.wonkglorg.utilitylib.managers.PluginManager;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Heads extends JavaPlugin
{
	private static PluginManager pluginManager;
	private static Heads heads;
	
	private static final List<Player> playerChatListener = new ArrayList<>();
	
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
		
		new HeadConfig(this, "headconfig");
	}
	
	private void addListeners()
	{
		new DamageListener(this);
		new DeathListener(this);
		new ClickListener(this);
		new ExplosionEvent(this);
		new HeadPickupListener(this);
		new ChatEvent(this);
		
		
		//Add config to do events on head drops, like fireworks, other particle effects etc
		
		//add sql  to save data of all skulls names / owner / enchants etc in case they get placed and picked up again
		
	}
	
	private void addConfigs()
	{
		pluginManager.addConfig(new ConfigYML(this, YML.CONFIG.getFileName()));
		pluginManager.addConfig(new ConfigYML(this, YML.HEAD_DATA.getFileName()));
		pluginManager.addConfig(new ConfigYML(this, YML.HEAD_DROP_NUMBERS.getFileName()));
		
		pluginManager.setDefaultLang(Locale.ENGLISH, new ConfigYML(this, "eng.yml", "lang"));
		//pluginManager.getLangManager().addAllLangFilesFromPath(this, "lang/");
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
	
	public static boolean exists(Player player)
	{
		return playerChatListener.contains(player);
	}
	
	public static void remove(Player player)
	{
		playerChatListener.remove(player);
	}
	
	public static void add(Player player){
		playerChatListener.add(player);
	}
}
