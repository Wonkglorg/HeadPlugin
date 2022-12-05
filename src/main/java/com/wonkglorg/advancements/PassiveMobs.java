package com.wonkglorg.advancements;

import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utilitylib.utils.players.PlayerUtil;
import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import eu.endercentral.crazy_advancements.advancement.AdvancementReward;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveMobs implements Listener
{
	private final AdvancementManager manager = new AdvancementManager(new NameKey("WonkyHeads", "manager"));
	private final JavaPlugin plugin;
	public PassiveMobs(JavaPlugin plugin)
	{
		this.plugin = plugin;
		ItemStack icon = new ItemStack(Material.STONE);
		JSONMessage title = new JSONMessage(new TextComponent("Cow"));
		JSONMessage description = new JSONMessage(new TextComponent("Description"));
		AdvancementFrame frame = AdvancementFrame.TASK;
		AdvancementVisibility visibility = AdvancementVisibility.ALWAYS;
		AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, visibility);
		display.setBackgroundTexture("textures/block/structure_block.png");
		display.setX(1);
		display.setY(1.5F);
		
		Advancement advancement = new Advancement(new NameKey("WonkyHeads", "test_advancement"), display, AdvancementFlag.TOAST_AND_MESSAGE);
		advancement.setCriteria(new Criteria(5));
		advancement.setReward(new AdvancementReward()
		{
			@Override
			public void onGrant(Player player)
			{
				PlayerUtil.give(player, new ItemBuilder(Material.DIRT).setName("Epic Dirt").build());
			}
		});
		
		manager.addAdvancement(advancement);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.broadcast(Message.color("Added Player"));
				manager.loadProgress(e.getPlayer());
				manager.addPlayer(e.getPlayer());
			}
		}.runTaskLater(plugin, 1);
	}
	
}