package com.wonkglorg.advancements;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.WonkyHeadValues;
import com.wonkglorg.enums.YML;
import com.wonkglorg.heads.MobHeadData;
import com.wonkglorg.heads.MobHeadDataUtility;
import com.wonkglorg.utilitylib.config.Config;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AdvancementData
{
	private NameKey key;
	private Advancement advancement;
	private Criteria criteria;
	private String path;
	private String title;
	private String description;
	private ItemStack icon;
	private double x;
	private double y;
	private AdvancementVisibility visibility;
	private AdvancementFrame frame;
	private String texture;
	private Config config;
	private Advancement parent;
	private AdvancementFlag flag;
	private boolean enabled;
	
	public AdvancementData(String path, String key, Config config)
	{
		this.path = path;
		this.key = new NameKey(WonkyHeadValues.PLUGIN.getKey(), key);
		this.config = config;
		title = config.getString(path + ".title");
		description = config.getString(path + ".description");
		icon = createIcon(Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()), config.getString(path + ".icon"));
		x = config.getDouble(path + ".x");
		y = config.getDouble(path + ".y");
		frame = createFrame(config.getString(path + ".frame"));
		visibility = createVisibility(config.getString(path + ".visibility"));
		flag = createFlag(config.getString(path + ".flag"));
		texture = config.getString(path + ".texture");
		enabled = config.getBoolean(path + ".enabled");
	}
	
	public boolean hasCriteria(String criteria)
	{
		//if the criteria is equal to a material return that, otherwise check for head that fits
		return this.criteria.getCriteria().containsKey(criteria);
	}
	
	public void createAdvancement()
	{
		
		//HANDLE ADVANCEMENT CREATION IN A DIFFERENT WAY
		if(key == null)
		{
			return;
		}
		advancement = new Advancement(parent, key, createDisplay(), flag);
	}
	
	public static void createNewDirectory(Config config, String path, String name)
	{
		String comPath = path + "." + name;
		if(config.contains(comPath))
		{
			return;
		}
		config.set(comPath + "." + "title", "Enter value");
		config.set(comPath + "." + "description", "Enter value");
		config.set(comPath + "." + "frame", "TASK");
		config.set(comPath + "." + "visibility", "ALWAYS");
		config.set(comPath + "." + "x", 1);
		config.set(comPath + "." + "y", 1);
		config.set(comPath + "." + "icon", "Heads.Sheep.Red");
		config.set(comPath + "." + "parent", null);
		config.set(comPath + "." + "advancementFlag", "SHOW_TOAST");
		config.set(comPath + "." + "Enabled", true);
		config.set(comPath + "." + "criteria.values", " ");
		config.set(comPath + "." + "criteria.requirements", " ");
		config.silentSave();
	}
	
	private AdvancementDisplay createDisplay()
	{
		
		if(icon == null)
		{
			icon = new ItemStack(Material.STONE);
		}
		AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, visibility);
		display.setBackgroundTexture(texture);
		display.setY((float) x);
		display.setX((float) y);
		return display;
	}
	
	private AdvancementFrame createFrame(String value)
	{
		if(value == null)
		{
			return AdvancementFrame.TASK;
		}
		AdvancementFrame frame;
		try
		{
			frame = AdvancementFrame.valueOf(value);
			
		} catch(IllegalArgumentException ignored)
		{
			frame = AdvancementFrame.TASK;
		}
		return frame;
	}
	
	private AdvancementVisibility createVisibility(String value)
	{
		if(value == null)
		{
			return AdvancementVisibility.VANILLA;
		}
		
		return AdvancementVisibility.parseVisibility(value);
	}
	
	private AdvancementFlag createFlag(String value)
	{
		if(value == null)
		{
			return AdvancementFlag.SHOW_TOAST;
		}
		try
		{
			return AdvancementFlag.valueOf(value);
		} catch(IllegalArgumentException ignore)
		{
			return AdvancementFlag.SHOW_TOAST;
		}
	}
	
	private Criteria createCriteria(Config config, String path)
	{
		String[] values = config.getStringList(path + ".values").toArray(new String[0]);
		String[] requirement = config.getSection(path + ".requirements", false).toArray(new String[0]);
		
		String[][] requirements = new String[requirement.length][];
		int i = 0;
		for(String s : requirement)
		{
			
			String[] subRequirement = config.getStringList(path + ".requirements." + s).toArray(new String[0]);
			requirements[i] = subRequirement;
			i++;
		}
		return new Criteria(values, requirements);
	}
	
	private ItemStack createIcon(Config config, String path)
	{
		if(path == null)
		{
			return new ItemStack(Material.STONE);
		}
		MobHeadData mobHeadData = MobHeadDataUtility.getFirstValidConfigHeadData(config, path);
		if(mobHeadData != null)
		{
			return mobHeadData.createHeadItem();
		}
		Material material;
		try
		{
			material = Material.valueOf(path);
		} catch(IllegalArgumentException e)
		{
			material = Material.STONE;
		}
		return new ItemStack(material);
	}
	
	public void setFrame(AdvancementFrame frame)
	{
		this.frame = frame;
	}
	
	public void setFrame(String frame)
	{
		this.frame = createFrame(frame);
	}
	
	public void setVisibility(AdvancementVisibility visibility)
	{
		this.visibility = visibility;
	}
	
	public void setVisibility(String visibility)
	{
		this.visibility = createVisibility(visibility);
	}
	
	public void setFlag(AdvancementFlag flag)
	{
		this.flag = flag;
	}
	
	public void setFlag(String flag)
	{
		this.flag = createFlag(flag);
	}
	
	public void setCriteria(Criteria criteria)
	{
		this.criteria = criteria;
	}
	
	public void setCriteria(String criteria)
	{
		this.criteria = createCriteria(config, criteria);
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	public NameKey getKey()
	{
		return key;
	}
	
	public Advancement getAdvancement()
	{
		return advancement;
	}
	
	public Criteria getCriteria()
	{
		return criteria;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public ItemStack getIcon()
	{
		return icon;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public AdvancementVisibility getVisibility()
	{
		return visibility;
	}
	
	public AdvancementFrame getFrame()
	{
		return frame;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public Config getConfig()
	{
		return config;
	}
	
	public Advancement getParent()
	{
		return parent;
	}
	
	public AdvancementFlag getFlag()
	{
		return flag;
	}
	
}