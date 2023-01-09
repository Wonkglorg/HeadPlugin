package com.wonkglorg;

import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.command.OpenMenuGui;
import com.wonkglorg.command.config.ReloadConfigs;
import com.wonkglorg.command.creeper_spawner.ChargedCreeper;
import com.wonkglorg.command.creeper_spawner.ClickListener;
import com.wonkglorg.command.creeper_spawner.ExplosionEvent;
import com.wonkglorg.command.dropchance.DropChance;
import com.wonkglorg.command.headgive.GiveCustomHead;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.command.value.ChangeValueCommand;
import com.wonkglorg.enums.YML;
import com.wonkglorg.listeners.advancements.AdvancementListener;
import com.wonkglorg.listeners.advancements.JoinListener;
import com.wonkglorg.listeners.heads.DamageListener;
import com.wonkglorg.listeners.heads.DeathListener;
import com.wonkglorg.utilitylib.UtilityPlugin;
import com.wonkglorg.utilitylib.config.ConfigYML;
import com.wonkglorg.utilitylib.managers.PluginManager;
import com.wonkglorg.utilitylib.message.ChatColor;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class Heads extends UtilityPlugin
{
	
	//CLEANUP AND REWORK ALL CODE!!!!!!!!! VERY MESSY RN
	
	//WORLDS HAVE BEEN ADDED TO HEADATA BUT ARE NOT USED YET NOR FINISHED ADDING IN HEAD YML
	
	//FINISH ADVANCEMENTS NOW WORKING RN WHY?
	
	//ADD OPTION TO DROP HEADS FROM OTHER THINGS LIKE FOUND IN CHESTS WANDERING TRADERS, VILLAGERS, FROM BLOCK BREAK ADD NEW MENU FOR THIS
	
	// IF PLAYER HAS PERMISSION X LET THEM DROP HEADS??? IDK HOW TO INCLUDE THAT
	private static Heads plugin;
	private boolean advancementApi = false;
	private AdvancementHandler advancementHandler;
	private AdvancementManager advancementManager;
	@Override
	public void loadBefore()
	{
		plugin = this;
		
		if(dependencyExists("CrazyAdvancementsAPI"))
		{
			advancementApi = true;
			advancementHandler = new AdvancementHandler(this);
			advancementManager = advancementHandler.getManager();
		}
		
	}
	
	@Override
	public void pluginStartup()
	{
		//copyValuesFromYmlToSQL();
		System.out.println("--------------------------Add Advancements ----------------------------");
		advancementHandler.startup(manager.getConfigManager().getConfig(YML.ADVANCEMENTS.getFileName()));
		System.out.println("--------------------------finished Advancements ----------------------------");
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
		if(advancementApiExists())
		{
			manager.add(new JoinListener(this, advancementManager));
			manager.add(new AdvancementListener(this, advancementHandler));
		}
		manager.add(new ExplosionEvent(this));
	}
	
	@Override
	public void command()
	{
		manager.add(new OpenMenuGui(this, "HeadGui"));
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
		for(YML yml : YML.values())
		{
			manager.add(new ConfigYML(this, yml.getFileName()));
		}
		if(advancementApiExists())
		{
			manager.add(new ConfigYML(this, "advancements.yml"));
		}
		
	}
	
	@Override
	public void lang()
	{
		manager.addDefaultLang(Locale.ENGLISH, new ConfigYML(this, "eng.yml", "lang"));
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
	
	public void advancements()
	{
	}
	
	public static JavaPlugin getInstance()
	{
		return plugin;
	}
	
	public boolean advancementApiExists()
	{
		return advancementApi;
	}
	
	public PluginManager manager()
	{
		return manager;
	}
	
	public AdvancementHandler getAdvancementHandler()
	{
		return advancementHandler;
	}
	
	// TRACK PLAYERS ADVAMCENTS BY GIVING HEAD UNIQUE PROPERTY IF PICKED UP FOR FIRST TIME THEY GET PROGRESS
	
	//THINK OF METHOD TO ALLOW REPICKUP OF HEADS AND KEEP DATA ON THE HEAD
	
	//TRACK TOTAL PICKED UP HEADS?
	
	//HEAD ONLY GIVES ADVANCEMENT 1 TIME for the person who picked it up initially no one else.
}
