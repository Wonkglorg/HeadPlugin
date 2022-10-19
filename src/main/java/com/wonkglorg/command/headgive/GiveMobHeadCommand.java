package com.wonkglorg.command.headgive;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GiveMobHeadCommand extends Command
{
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	private final List<EntityType> all = new ArrayList<>();
	private final List<EntityType> hostiles = new ArrayList<>();
	private final List<EntityType> passives = new ArrayList<>();
	private final List<EntityType> animals = new ArrayList<>();
	private final List<EntityType> bosses = new ArrayList<>();
	private final List<String> completions = new ArrayList<>();
	
	private final HashMap<String, List<EntityType>> handlerMap = new HashMap<>();
	private final Config config;
	
	public GiveMobHeadCommand(@NotNull JavaPlugin main, @NotNull String name)
	{
		super(main, name);
		config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
		initLists();
		initHandleMap();
	}
	
	@Override
	public boolean allowConsole()
	{
		return false;
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		if(args.length < 1)
		{
			return false;
		}
		
		if(args.length == 1)
		{
			addHeads(argAsString(0), player);
			return true;
		}
		addHeads(argAsString(0), argAsPlayer(1));
		return true;
	}
	
	List<String> emptyList = new ArrayList<>();
	
	@Override
	public List<String> tabComplete(@NotNull Player player, String[] args)
	{
		List<String> returnValues = new ArrayList<>();
		if(args.length == 1)
		{
			StringUtil.copyPartialMatches(args[0], completions, returnValues);
			Collections.sort(returnValues);
			return returnValues;
		}
		if(args.length == 2)
		{
			return null;
		}
		return emptyList;
	}
	
	private void addHeads(String filterType, Player player)
	{
		if(player == null)
		{
			return;
		}
		int droppedHeads = 0;
		List<EntityType> filter;
		filter = handlerMap.get(filterType);
		filter = filter == null ? Collections.singletonList(EntityType.valueOf(filterType)) : filter;
		for(EntityType entity : EntityType.values())
		{
			if(!filter.contains(entity))
			{
				continue;
			}
			
			for(MobHeadData headData : MobHeadData.getAllValidConfigHeadData(config, "Heads." + entity.name().toLowerCase().replaceAll("_", " ")))
			{
				if(!headData.isEnabled())
				{
					continue;
				}
				ItemUtility.give(player, ItemUtility.createCustomHead(headData.getTexture(), headData.getName(), headData.getDescription()));
				droppedHeads++;
			}
		}
		Message.msgPlayer(player, lang.getValue(player, "command-head-drop-success").replace("<headcount>", String.valueOf(droppedHeads)));
		if(droppedHeads > 200)
		{
			Message.msgPlayer(player, lang.getValue(player, "command-head-drop-warn"));
		}
	}
	
	private void initHandleMap()
	{
		handlerMap.put("ALL", all);
		handlerMap.put("ANIMAL", animals);
		handlerMap.put("BOSS", bosses);
		handlerMap.put("PASSIVE", passives);
		handlerMap.put("HOSTILE", hostiles);
		
	}
	
	private void initLists()
	{
		initPassiveList();
		initHostileList();
		initAnimalList();
		initBossList();
		initAllList();
		initCompletionList();
	}
	
	private void initCompletionList()
	{
		completions.addAll(Arrays.asList("ALL", "HOSTILE", "ANIMAL", "PASSIVE", "BOSS"));
		all.forEach(entity ->
		{
			if(!completions.contains(entity.name()))
			{
				completions.add(entity.name());
			}
		});
	}
	
	private void initAllList()
	{
		addToList(hostiles, all);
		addToList(animals, all);
		addToList(passives, all);
		addToList(bosses, all);
	}
	
	private void initHostileList()
	{
		hostiles.addAll(Arrays.asList(EntityType.BLAZE,
				EntityType.SPIDER,
				EntityType.CAVE_SPIDER,
				EntityType.CREEPER,
				EntityType.DROWNED,
				EntityType.ELDER_GUARDIAN,
				EntityType.ENDER_DRAGON,
				EntityType.ENDERMITE,
				EntityType.EVOKER,
				EntityType.GHAST,
				EntityType.GUARDIAN,
				EntityType.HOGLIN,
				EntityType.HUSK,
				EntityType.MAGMA_CUBE,
				EntityType.PHANTOM,
				EntityType.PIGLIN_BRUTE,
				EntityType.RAVAGER,
				EntityType.SHULKER,
				EntityType.SILVERFISH,
				EntityType.SKELETON,
				EntityType.SLIME,
				EntityType.SLIME,
				EntityType.STRAY,
				EntityType.VEX,
				EntityType.VINDICATOR,
				EntityType.WARDEN,
				EntityType.WITCH,
				EntityType.WITHER,
				EntityType.WITHER_SKELETON,
				EntityType.ZOGLIN,
				EntityType.ZOMBIE,
				EntityType.ZOMBIE_VILLAGER));
	}
	
	private void initPassiveList()
	{
		passives.addAll(Arrays.asList(EntityType.ALLAY,
				EntityType.AXOLOTL,
				EntityType.BAT,
				EntityType.CAT,
				EntityType.CHICKEN,
				EntityType.COD,
				EntityType.COW,
				EntityType.DONKEY,
				EntityType.FOX,
				EntityType.FROG,
				EntityType.GLOW_SQUID,
				EntityType.HORSE,
				EntityType.MUSHROOM_COW,
				EntityType.MULE,
				EntityType.OCELOT,
				EntityType.PARROT,
				EntityType.PUFFERFISH,
				EntityType.RABBIT,
				EntityType.SALMON,
				EntityType.SHEEP,
				EntityType.SKELETON_HORSE,
				EntityType.IRON_GOLEM,
				EntityType.SQUID,
				EntityType.STRIDER,
				EntityType.TADPOLE,
				EntityType.TROPICAL_FISH,
				EntityType.TURTLE,
				EntityType.VILLAGER,
				EntityType.WANDERING_TRADER,
				EntityType.ZOMBIE_HORSE,
				EntityType.SNOWMAN));
	}
	
	private void initAnimalList()
	{
		animals.addAll(Arrays.asList(EntityType.AXOLOTL,
				EntityType.BEE,
				EntityType.CAT,
				EntityType.CHICKEN,
				EntityType.COW,
				EntityType.DONKEY,
				EntityType.FOX,
				EntityType.FROG,
				EntityType.GOAT,
				EntityType.HOGLIN,
				EntityType.HORSE,
				EntityType.LLAMA,
				EntityType.MUSHROOM_COW,
				EntityType.MULE,
				EntityType.OCELOT,
				EntityType.PANDA,
				EntityType.PIG,
				EntityType.RABBIT,
				EntityType.SHEEP,
				EntityType.STRIDER,
				EntityType.TRADER_LLAMA,
				EntityType.TURTLE,
				EntityType.WOLF,
				EntityType.BAT,
				EntityType.COD,
				EntityType.DOLPHIN,
				EntityType.GLOW_SQUID,
				EntityType.POLAR_BEAR,
				EntityType.PUFFERFISH,
				EntityType.SALMON,
				EntityType.SQUID,
				EntityType.TROPICAL_FISH));
	}
	
	private void addToList(List<EntityType> entityTypeList, List<EntityType> copyToList)
	{
		entityTypeList.forEach(entity ->
		{
			if(!copyToList.contains(entity))
			{
				copyToList.add(entity);
			}
		});
	}
	
	private void initBossList()
	{
		bosses.addAll(Arrays.asList(EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.ELDER_GUARDIAN));
	}
}

