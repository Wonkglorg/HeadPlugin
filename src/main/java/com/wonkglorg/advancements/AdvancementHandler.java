package com.wonkglorg.advancements;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.config.Config;
import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Stack;

public class AdvancementHandler
{
	private final Heads plugin;
	private final AdvancementManager manager = new AdvancementManager(new NameKey("WonkyHeads", "manager"));
	private final HashMap<String, AdvancementData> advancements = new HashMap<>();
	private final Stack<String> advancementQueue = new Stack<>();
	
	public AdvancementHandler(Heads plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Loads all advancements into memory and adds them to the server
	 *
	 * @param config
	 */
	public void startup(Config config)
	{
		addAdvancements(config);
		loadAllAdvancements();
	}
	
	public void addAdvancements(Config config)
	{
		String path = "advancements";
		System.out.println(config.getSection(path, false));
		for(String section : config.getSection(path, false))
		{
			if(advancements.containsKey(section))
			{
				continue;
			}
			path = "advancements";
			path = path + "." + section;
			checkParent(config, path, section);
			//adds 1 to advancement stack, and after it finds highest parent work through them and add the child of the parent 1 by 1
			while(!advancementQueue.isEmpty())
			{
				String advancementS = advancementQueue.pop();
				addAdvancement(config, "advancements." + advancementS, advancementS);
			}
			AdvancementData data = new AdvancementData(path, section, config);
			System.out.println(data);
			advancements.put(section, data);
		}
	}
	
	/**
	 * checks if a parent exists and if yes if it is already initialized
	 *
	 * @param config
	 * @param path
	 * @param name
	 */
	private void checkParent(Config config, String path, String name)
	{
		
		String parent = config.getString(path + ".parent");
		if(parent == null)
		{
			return;
		}
		if(advancements.containsKey(parent))
		{
			return;
		}
		advancementQueue.add(name);
		addAdvancement(config, path, name);
	}
	
	/**
	 * adds and advancement, calls parent check before adding it
	 *
	 * @param config
	 * @param path
	 * @param name
	 */
	public void addAdvancement(Config config, String path, String name)
	{
		
		if(advancements.containsKey(name))
		{
			return;
		}
		checkParent(config, path, config.getString(path + ".parent"));
		AdvancementData data = new AdvancementData(path, name, config);
		advancements.put(name, data);
	}
	
	public void loadAllAdvancements()
	{
		advancements.forEach(((s, advancementData) -> advancementData.createAdvancement()));
	}
	
	public void createNewAdvancement()
	{
		
		//create default advancement directory with entered name from chat
		
		//if clicked go to menu and type /value to add a new name to it, if name is set return to menu you came from
		//new AdvancementData();
	}
	/*
	private Advancement convertValuesToAdvancement(Config config, String path, String baseName)
	{
		ItemStack icon = createIcon(plugin.manager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()), config.getString(path + ".Icon"));
		String title = config.getString(path + ".Name");
		String description = config.getString(path + ".Description");
		AdvancementFrame frame = createFrame(config.getString(path + ".Frame"));
		AdvancementVisibility visibility = createVisibility(config.getString(path + ".Visibility"));
		float x = (float) config.getDouble(path + ".X");
		float y = (float) config.getDouble(path + ".Y");
		String texture = config.getString(path + ".Texture");
		String parent = config.getString(path + ".Parent");
		AdvancementDisplay display = createDisplay(icon, title, description, frame, visibility, x, y, texture);
		
		String flag = config.getString(path + ".AdvancementFlag");
		
		//find way to add all the ones without parent first and then go from there, so order does not matter how they got added
		return createAdvancement(advancements.get(parent).advancement(), baseName, display, flag);
	}
	
	 */
	
	private AdvancementDisplay createDisplay(ItemStack icon,
											 String title,
											 String desc,
											 AdvancementFrame frame,
											 AdvancementVisibility visibility,
											 float x,
											 float y,
											 String texture)
	{
		JSONMessage titleJ = new JSONMessage(new TextComponent(title));
		JSONMessage descriptionJ = new JSONMessage(new TextComponent(desc));
		
		AdvancementDisplay display = new AdvancementDisplay(icon, titleJ, descriptionJ, frame, visibility);
		display.setX(x);
		display.setY(y);
		display.setBackgroundTexture(texture);
		return display;
	}
	
	private Criteria createCriteria(Config config, String path)
	{
		String[] values = config.getStringList(path + ".Values").toArray(new String[0]);
		//get arrays of arrays and add them to requirements
		String[][] requirements = {{}};
		return new Criteria(values, requirements);
	}
	
	private Advancement createAdvancement(Advancement parent, String keyName, AdvancementDisplay display, String advancementFlag)
	{
		AdvancementFlag flag;
		try
		{
			flag = AdvancementFlag.valueOf(advancementFlag);
		} catch(IllegalArgumentException e)
		{
			flag = AdvancementFlag.SHOW_TOAST;
		}
		return new Advancement(parent, new NameKey("WonkyHeads", keyName), display, flag);
	}
	
	//TRACK MOB HEAD DATA WITH PERSISTENT DATA USING HEAD NAME?
	
	public AdvancementManager getManager()
	{
		return manager;
	}
	
	public HashMap<String, AdvancementData> getAdvancements()
	{
		return advancements;
	}
	
}