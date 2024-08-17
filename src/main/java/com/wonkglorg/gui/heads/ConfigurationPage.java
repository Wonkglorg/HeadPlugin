package com.wonkglorg.gui.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.command.value.ChangeValueCommand;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.utilitylib.base.item.ItemUtil;
import com.wonkglorg.utilitylib.base.message.ChatColor;
import com.wonkglorg.utilitylib.base.message.Message;
import com.wonkglorg.utilitylib.base.message.color_components.Color;
import com.wonkglorg.utilitylib.base.players.PlayerUtil;
import com.wonkglorg.utilitylib.builder.ItemBuilder;
import com.wonkglorg.utilitylib.inventory.Button;
import com.wonkglorg.utilitylib.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.manager.config.Config;
import com.wonkglorg.utilitylib.manager.config.ConfigYML;
import com.wonkglorg.utilitylib.manager.managers.LangManager;
import com.wonkglorg.utils.HeadProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfigurationPage extends InventoryGUI {
    private final Heads plugin;
    private final MobHeadData headData;
    private final LangManager lang = Heads.manager().getLangManager();
    private final Config backupConfig = Heads.manager().getConfigManager().getConfig(YML.HEAD_DATA_BACKUP.getFileName());
    private final Player player;
    private final ConfigYML config = (ConfigYML) Heads.manager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
    private final boolean changes;
    private boolean resetConfirmed = false;

    public ConfigurationPage(Heads plugin, HeadProfile menuUtility, boolean changes) {
        super(54, menuUtility.getMobHeadData().getName(), Heads.getPlugin(Heads.class), menuUtility);
        player = menuUtility.getOwner();
        this.changes = changes;
        this.plugin = plugin;
        headData = menuUtility.getMobHeadData();
    }

    // IMPLEMENT WAY TO ONLY ALLOW HEADS TO DROP FROM SPECIFIC SOURCE LIKE PLAYER KILL OR CREEPER
    @Override
    public void addComponents() {
        fill(FILLER);
        addButton(headShowcase(headData.createHeadItemWithInfoDesc()), 4);
        addButton(changeName(), 19);
        addButton(changeDescription(), 22);
        addButton(changeTexture(), 25);
        addButton(setEnabled((HeadProfile) profile, this), 40);
        addButton(dropChance((HeadProfile) profile, this), 43);
        String defaultPath = config.getParentPath(headData.getPath());
        if (MobHeadData.isValidHeadPath(backupConfig, defaultPath + ".default")) {
            addButton(resetHeadToDefault(headData.getPath(), defaultPath + ".default"), 41);
        }
        if (changes) {
            addButton(accept(this), 45);
        }
        addButton(back((this)), 49);
    }

    private Button headShowcase(ItemStack itemStack) {
        return new Button(itemStack) {
            @Override
            public void onClick(InventoryClickEvent e) {
                PlayerUtil.give((Player) e.getWhoClicked(), headData.createHeadItem());
            }
        };
    }

    private Button accept(ConfigurationPage headConfigurationPage) {
        ItemStack icon = new ItemBuilder(Material.LIME_CONCRETE).setName(Message.color("Confirm")).build();
        return new Button(new ItemStack(icon)) {
            @Override
            public void onClick(InventoryClickEvent e) {
                HeadProfile headMenuUtility = (HeadProfile) profile;
                headMenuUtility.getMobHeadData().writeToConfig();
                Message.msgPlayer(profile.getOwner(), "Successfully applied changes");
                headConfigurationPage.destroy();
                destroy();
                new HeadMenuPage(plugin, headMenuUtility, config, config.getParentPath(headMenuUtility.getMobHeadData().getPath()), null, 1).open();
                headMenuUtility.setMobHeadData(null);
            }
        };
    }

    private Button back(ConfigurationPage headConfigurationPage) {
        ItemStack icon = new ItemBuilder(Material.BARRIER).setName(Message.color("Back")).build();
        return new Button(icon) {
            @Override
            public void onClick(InventoryClickEvent e) {
                HeadProfile headMenuUtility = (HeadProfile) profile;
                headConfigurationPage.destroy();
                new HeadMenuPage(plugin, headMenuUtility, config, config.getParentPath(headMenuUtility.getMobHeadData().getPath()), null, 1).open();
                headMenuUtility.setMobHeadData(null);
                destroy();
            }
        };
    }

    private Button setEnabled(HeadProfile menuUtility, ConfigurationPage configurationPage) {
        ItemStack icon = menuUtility.getMobHeadData().isEnabled()
                ? new ItemBuilder(Material.LIME_CONCRETE).setName(Message.color("Enabled")).build()
                : new ItemBuilder(Material.RED_CONCRETE).setName(Message.color("Disabled")).build();
        return new Button(icon) {
            @Override
            public void onClick(InventoryClickEvent e) {
                menuUtility.getMobHeadData().setEnabled(!menuUtility.getMobHeadData().isEnabled());
                setItem(menuUtility.getMobHeadData().isEnabled()
                        ? new ItemBuilder(Material.LIME_CONCRETE).setName(Message.color("Enabled")).build()
                        : new ItemBuilder(Material.RED_CONCRETE).setName(Message.color("Disabled")).build());
                addButton(accept(configurationPage), 45);
                update();
            }
        };
    }

    private Button changeName() {
        ItemStack icon = new ItemBuilder(Material.OAK_SIGN).setName(Message.color("Change Name")).build();
        return new Button(icon) {
            @Override
            public void onClick(InventoryClickEvent e) {
                handleChange(MenuDataVariables.NAME);
                destroy();
                getInventory().close();
            }
        };
    }

    private Button changeDescription() {
        ItemStack icon = new ItemBuilder(Material.OAK_SAPLING).setName(Message.color("Change Description")).build();
        return new Button(new ItemStack(icon)) {
            @Override
            public void onClick(InventoryClickEvent e) {
                handleChange(MenuDataVariables.DESCRIPTION);
                destroy();
                getInventory().close();
            }
        };
    }

    private Button resetHeadToDefault(String path, String backupDefaultPath) {

        ItemStack icon = new ItemBuilder(Material.BEACON).setName(Message.color("Reset to default values")).build();
        return new Button(icon) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (resetConfirmed) {
                    headData.setValuesFromHeadData(new MobHeadData(backupDefaultPath, backupConfig, 1));
                    headData.setPath(path);
                    headData.writeToConfig();
                    setItem(icon);
                    destroy();
                    new ConfigurationPage(plugin, (HeadProfile) profile, false).open();
                    update();
                    resetConfirmed = false;
                    return;
                }
                resetConfirmed = true;
                ItemUtil.rename(icon, "Confirm reset?");
                setItem(icon);
                update();
            }
        };
    }

    private Button changeTexture() {
        ItemStack icon = new ItemBuilder(Material.BEACON).setName(Message.color("Change Texture")).build();
        return new Button(new ItemStack(icon)) {
            @Override
            public void onClick(InventoryClickEvent e) {
                handleChange(MenuDataVariables.TEXTURE);
                destroy();
                getInventory().close();
            }
        };
    }

    private Button dropChance(HeadProfile menuUtility, ConfigurationPage configurationPage) {
        return new Button(getChanceItemStack(menuUtility)) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.getClick() == ClickType.LEFT) {
                    handleChange(MenuDataVariables.DROPCHANCE);
                    destroy();
                    update();
                    getInventory().close();
                }
                setItem(getChanceItemStack(menuUtility));
                addButton(accept(configurationPage), 45);
                update();
            }
        };
    }


    private ItemStack getChanceItemStack(HeadProfile profile) {
        double dropchance = profile.getMobHeadData().getDropChance();
        String hexColor = Color.getHexColorBetween(0,
                100,
                dropchance,
                new Color(java.awt.Color.RED),
                new Color(java.awt.Color.YELLOW),
                new Color(java.awt.Color.GREEN));
        return new ItemBuilder(Material.LIGHT).setName(Message.color("Dropchance")).addLoreLine(Message.color(ChatColor.hexToChatColor(hexColor) + dropchance + "%")).addLoreLine(
                Message.color(ChatColor.GREEN + "Left click to set value")).build();
    }

    private void handleChange(MenuDataVariables menuDataVariables) {
        HeadProfile headData = (HeadProfile) profile;
        headData.setDataVariables(menuDataVariables);
        ChangeValueCommand.setPlayerDataChange(headData.getOwner(), this.headData);
        switch (menuDataVariables) {
            case DESCRIPTION ->
                    Message.msgPlayer(profile.getOwner(), lang.getValue(player, "command-request-change-description"));
            case TEXTURE ->
                    Message.msgPlayer(profile.getOwner(), lang.getValue(player, "command-request-change-texture"));
            case NAME -> Message.msgPlayer(profile.getOwner(), lang.getValue(player, "command-request-change-name"));
            case DROPCHANCE ->
                    Message.msgPlayer(profile.getOwner(), lang.getValue(player, "command-request-change-dropchance"));
        }
        Message.msgPlayer(profile.getOwner(), lang.getValue(player, "command-request-cancel"));
    }

}