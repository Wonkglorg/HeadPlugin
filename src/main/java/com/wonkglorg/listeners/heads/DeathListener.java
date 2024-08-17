package com.wonkglorg.listeners.heads;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.entityProcessor.EntityProcessor;
import com.wonkglorg.heads.entityProcessor.EntityProcessorEnums;
import com.wonkglorg.heads.entityProcessor.EntityTypeProcessor;
import com.wonkglorg.utilitylib.manager.config.Config;
import com.wonkglorg.utils.DefaultHashMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DeathListener implements Listener {
    HashMap<EntityType, EntityTypeProcessor> entityProcessorMap = new DefaultHashMap<>(new EntityProcessor());
    Config config = Heads.manager().getConfigManager().getConfig(YML.CONFIG.getFileName());

    private final JavaPlugin plugin;

    public DeathListener(JavaPlugin plugin) {
        this.plugin = plugin;
        addProcessors();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Entity mob = e.getEntity();
        if (!Objects.equals(mob.getPersistentDataContainer().get(new NamespacedKey(plugin, "drophead"), PersistentDataType.STRING), "true")) {
            return;
        }
        //TEST IF THIS WORKS!!!!!!!!!!!!!!!!!!!!!
        entityProcessorMap.get(mob.getType()).process(mob, e.getEntity().getLocation());
        removeDefaultHeadDrops(e.getDrops(), e.getEntity().getType());

    }

    private void removeDefaultHeadDrops(List<ItemStack> drops, EntityType entity) {
        if (entity == EntityType.CREEPER && config.getBoolean("Remove_Vanilla_Creeper_Head")) {
            drops.remove(new ItemStack(Material.CREEPER_HEAD));
            return;
        }
        if (entity == EntityType.SKELETON && config.getBoolean("Remove_Vanilla_Skeleton_Head")) {
            drops.remove(new ItemStack(Material.SKELETON_SKULL));
            return;
        }
        if (entity == EntityType.ZOMBIE && config.getBoolean("Remove_Vanilla_Zombie_Head")) {
            drops.remove(new ItemStack(Material.ZOMBIE_HEAD));
        }
    }

    private void addProcessors() {
        //TEST IF THIS WORKS!!!!!!!!!!
        for (EntityProcessorEnums processor : EntityProcessorEnums.values()) {
            entityProcessorMap.put(processor.type(), processor.processor());
        }

    }
}