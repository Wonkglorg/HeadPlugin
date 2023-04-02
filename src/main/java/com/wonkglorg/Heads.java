package com.wonkglorg;

import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.command.OpenMenuGui;
import com.wonkglorg.command.config.ReloadConfigs;
import com.wonkglorg.command.dropchance.DropChance;
import com.wonkglorg.command.headgive.GiveCustomHead;
import com.wonkglorg.command.headgive.GiveMobHeadCommand;
import com.wonkglorg.command.value.ChangeValueCommand;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadDataUtility;
import com.wonkglorg.listeners.advancements.AdvancementListener;
import com.wonkglorg.listeners.advancements.JoinListener;
import com.wonkglorg.listeners.heads.DamageListener;
import com.wonkglorg.listeners.heads.DeathListener;
import com.wonkglorg.utilitylib.UtilityPlugin;
import com.wonkglorg.utilitylib.config.ConfigYML;
import com.wonkglorg.utilitylib.inventory.ProfileManager;
import com.wonkglorg.utilitylib.managers.PluginManager;
import com.wonkglorg.utilitylib.message.ChatColor;
import com.wonkglorg.utils.HeadProfile;
import com.wonkglorg.web.MainWebHandler;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class Heads extends UtilityPlugin
{
	private static Heads plugin;
	private boolean advancementApi = false;
	private AdvancementHandler advancementHandler;
	private AdvancementManager advancementManager;
	private static final ProfileManager<HeadProfile> profileManager = new ProfileManager<>(new HeadProfile(null));
	
	public void loadBefore()
	{
		new MainWebHandler();
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
		loadBefore();
		
		config();
		lang();
		event();
		command();
		
		advancementHandler.startup(manager.getConfigManager().getConfig(YML.ADVANCEMENTS.getFileName()));
		MobHeadDataUtility.updateYML();
	}
	
	@Override
	public void pluginShutdown()
	{
	
	}
	
	public void event()
	{
		manager.add(new DamageListener(this));
		manager.add(new DeathListener(this));
		if(advancementApiExists())
		{
			manager.add(new JoinListener(this, advancementManager));
			manager.add(new AdvancementListener(this, advancementHandler));
		}
	}
	
	public void command()
	{
		manager.add(new OpenMenuGui(this, "head-gui"));
		manager.add(new GiveCustomHead(this, "givecustomhead"));
		manager.add(new GiveMobHeadCommand(this, "givemobhead"));
		manager.add(new ReloadConfigs(this, "head-config-reload", manager.getConfigManager()));
		manager.add(new ChangeValueCommand(this, "value"));
		manager.add(new DropChance(this, "head-dropchance"));
	}
	
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
	
	public void lang()
	{
		manager.addDefaultLang(Locale.ENGLISH, new ConfigYML(this, "eng.yml", "lang"));
		manager.getLangManager().replace("<prefix>", ChatColor.GRAY + "[HeadPlugin]" + ChatColor.Reset);
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
	
	public static ProfileManager<HeadProfile> getProfileManager()
	{
		return profileManager;
	}
}
