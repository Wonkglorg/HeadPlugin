package com.wonkglorg.listeners.advancements;

import com.wonkglorg.advancements.AdvancementHandler;
import com.wonkglorg.enums.PersistentContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementListener implements Listener {
    private final AdvancementHandler manager;
    private final JavaPlugin plugin;

    public AdvancementListener(JavaPlugin plugin, AdvancementHandler manager) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        if (e.getItem().getItemStack().getType() != Material.PLAYER_HEAD) {
            return;
        }

        String criteria = e.getItem().getPersistentDataContainer().get(PersistentContainer.PERSISTENT_ADVANCEMENT.getNamespaceKey(),
                PersistentDataType.STRING);
        if (criteria == null) {
            return;
        }

        manager.grantAdvancementCriteria(player, criteria);

    }

}