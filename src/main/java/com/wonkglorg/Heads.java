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
import com.wonkglorg.utilitylib.base.message.ChatColor;
import com.wonkglorg.utilitylib.manager.UtilityPlugin;
import com.wonkglorg.utilitylib.manager.config.ConfigYML;
import com.wonkglorg.utilitylib.manager.config.LangConfig;
import com.wonkglorg.utilitylib.manager.managers.ProfileManager;
import com.wonkglorg.utils.HeadProfile;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Locale;

public final class Heads extends UtilityPlugin {
    private static Heads plugin;
    private boolean advancementApi = false;
    private AdvancementHandler advancementHandler;
    private AdvancementManager advancementManager;
    private static final ProfileManager<HeadProfile> profileManager = new ProfileManager<>(new HeadProfile(null));

    public void loadBefore() {
        //new MainWebHandler();
        plugin = this;
        if (dependencyExists("CrazyAdvancementsAPI")) {
            advancementApi = true;
            advancementHandler = new AdvancementHandler(this);
            advancementManager = advancementHandler.getManager();
        }
    }

    @Override
    public void pluginStartup() {
        loadBefore();

        loadConfigs();
        loadLangs();
        registerEvents();
        command();

        //database is corrupted on creation?

        //if table already exists make temp table update values from new one to the existing one then delete yourself
        //have seperate table for it? Make sure both stay updated?

        advancementHandler.startup(manager.getConfigManager().getConfig(YML.ADVANCEMENTS.getFileName()));
        MobHeadDataUtility.updateYML();
    }

    @Override
    public void pluginShutdown() {

    }

    public void registerEvents() {
        manager.add(new DamageListener(this));
        manager.add(new DeathListener(this));
        if (advancementApiExists()) {
            manager.add(new JoinListener(this, advancementManager));
            manager.add(new AdvancementListener(this, advancementHandler));
        }
    }

    public void command() {
        manager.add(new OpenMenuGui(this, "head-gui"));
        manager.add(new GiveCustomHead(this, "givecustomhead"));
        manager.add(new GiveMobHeadCommand(this, "givemobhead"));
        manager.add(new ReloadConfigs(this, "head-config-reload", manager.getConfigManager()));
        manager.add(new ChangeValueCommand(this, "value"));
        manager.add(new DropChance(this, "head-dropchance"));
    }

    public void loadConfigs() {
        for (YML yml : YML.values()) {
            manager.add(new ConfigYML(this, yml.getFileName()));
        }
        if (advancementApiExists()) {
            manager.add(new ConfigYML(this, "advancements.yml"));
        }

    }

    public void loadLangs() {
        manager.addDefaultLang(Locale.ENGLISH, new LangConfig(this, Path.of("lang", "eng.yml")));
        manager.getLangManager().replace("<prefix>", ChatColor.GRAY + "[HeadPlugin]" + ChatColor.Reset);
    }

    public static JavaPlugin getInstance() {
        return plugin;
    }

    public boolean advancementApiExists() {
        return advancementApi;
    }

    public AdvancementHandler getAdvancementHandler() {
        return advancementHandler;
    }

    public static ProfileManager<HeadProfile> getProfileManager() {
        return profileManager;
    }
}
