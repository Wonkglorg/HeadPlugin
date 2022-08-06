package com.wonkglorg.command;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.English;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.command.Command;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadUtils;
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
import java.util.Set;

public class HeadsCommand extends Command
{
	private final List<EntityType> all = new ArrayList<>();
	private final List<EntityType> hostiles = new ArrayList<>();
	private final List<EntityType> passives = new ArrayList<>();
	private final List<EntityType> animals = new ArrayList<>();
	private final List<EntityType> bosses = new ArrayList<>();
	private final List<String> completions = new ArrayList<>();
	
	private final HashMap<String, List<EntityType>> handlerMap = new HashMap<>();
	private final Config config;
	
	public HeadsCommand(@NotNull JavaPlugin main, @NotNull String name)
	{
		super(main, name);
		config = Heads.getManager().getConfig(YML.HEAD_DATA.getFileName());
		initLists();
		initHandleMap();
	}
	
	@Override
	public boolean execute(@NotNull Player player, String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		
		addHeads(args[0], player);
		
		return true;
	}
	
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
		return null;
	}
	
	private List<String> processPathValidation(String path)
	{
		Set<String> subHeads = config.getSection(path, true);
		List<String> validPaths = new ArrayList<>();
		if(!subHeads.isEmpty())
		{
			subHeads.forEach(s ->
			{
				if(config.getString(path + "." + s + ".Texture") != null)
				{
					validPaths.add(path + "." + s);
				}
				
			});
		}
		if(config.getString(path + ".Texture") != null)
		{
			validPaths.add(path);
		}
		return validPaths;
	}
	
	private void addHeads(String filterType, Player player)
	{
		int droppedHeads = 0;
		List<EntityType> filter;
		filter = handlerMap.get(filterType);
		if(filter == null)
		{
			filter = Collections.singletonList(EntityType.valueOf(filterType));
		}
		for(EntityType entity : EntityType.values())
		{
			if(!filter.contains(entity))
			{
				continue;
			}
			for(String paths : processPathValidation("Heads." + entity.name().toLowerCase().replaceAll("_", " ")))
			{
				if(!config.getBoolean(paths + ".Enabled"))
				{
					continue;
				}
				if(config.contains(paths + ".DropChance"))
				{
					double dropchance = config.getDouble(paths + ".DropChance");
					HeadUtils.dropHead(paths + ".Texture",
							paths + ".Name",
							paths + ".Description" + "|DropChance = " + dropchance,
							player.getLocation());
					droppedHeads++;
					continue;
				}
				droppedHeads++;
				HeadUtils.dropHead(paths + ".Texture", paths + ".Name", paths + ".Description", player.getLocation());
			}
		}
		Message.msgPlayer(player, English.COMMAND_HEAD_DROP_SUCCESS.toString().replace("<headcount>", String.valueOf(droppedHeads)));
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
		lambdaAdd(hostiles, all);
		lambdaAdd(animals, all);
		lambdaAdd(passives, all);
		lambdaAdd(bosses, all);
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
	
	private void lambdaAdd(List<EntityType> entityTypeList, List<EntityType> copyToList)
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

