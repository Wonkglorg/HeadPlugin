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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancementBuilder
{
	
	private final NameKey key;
	private Advancement advancement;
	private Criteria criteria;
	private ItemStack icon;
	private AdvancementDisplay display;
	private AdvancementVisibility visibility;
	private AdvancementFrame frame;
	private Advancement parent;
	private AdvancementFlag[] flag;
	private boolean enabled;
	private StringAdvancement stringAdvancement;
	private Map<String, String> criteriaLookupMap = new HashMap<>();
	
	public AdvancementBuilder(String key, String path, Config config)
	{
		stringAdvancement = new StringAdvancement(key, path, config);
		this.key = new NameKey(WonkyHeadValues.PLUGIN.getKey(), key);
	}
	
	public AdvancementBuilder setValuesFromConfig()
	{
		Config config = stringAdvancement.getConfig();
		String path = stringAdvancement.getPath();
		stringAdvancement.setTitle(config.getString(path + ".title"));
		stringAdvancement.setDescription(config.getString(path + ".description"));
		stringAdvancement.setX(config.getDouble(path + ".x"));
		stringAdvancement.setY(config.getDouble(path + ".y"));
		stringAdvancement.setTexture(config.getString(path + ".texture"));
		
		icon = createIcon(Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()), config.getString(path + ".icon"));
		frame = createFrame(config.getString(path + ".frame"));
		visibility = createVisibility(config.getString(path + ".visibility"));
		flag = createFlag(config.getString(path + ".flag"));
		enabled = config.getBoolean(path + ".enabled");
		
		for(String s : config.getSection(path + ".criteria", false))
		{
			String criteria = config.getString(path + ".criteria." + s);
			if(criteria == null)
			{
				continue;
			}
			
			criteriaLookupMap.put(criteria.toLowerCase(), s);
		}
		
		String[] values = criteriaLookupMap.values().toArray(new String[0]);
		
		List<List<String>> criteriaList = new ArrayList<>();
		
		for(String s : config.getSection(path + ".requirements", false))
		{
			criteriaList.add(config.getStringList(path + ".requirements." + s));
			
		}
		List<String>[] criteriaArray = listToNestedArray(criteriaList);
		setCriteria(values, listsTo2DArray(criteriaArray));
		return this;
	}
	
	public AdvancementBuilder setParent(Advancement parent)
	{
		this.parent = parent;
		return this;
	}
	
	public AdvancementBuilder setX(double x)
	{
		stringAdvancement.setX(x);
		return this;
	}
	
	public AdvancementBuilder setY(double y)
	{
		stringAdvancement.setY(y);
		return this;
	}
	
	public AdvancementBuilder setTexture(String texture)
	{
		stringAdvancement.setTexture(texture);
		return this;
	}
	
	public AdvancementBuilder setTitle(String title)
	{
		stringAdvancement.setTitle(title);
		return this;
	}
	
	public AdvancementBuilder setFrame(String frame)
	{
		this.frame = createFrame(frame);
		return this;
	}
	
	public AdvancementBuilder setVisibility(String visibility)
	{
		this.visibility = createVisibility(visibility);
		return this;
	}
	
	public AdvancementBuilder setFlag(String flag)
	{
		this.flag = createFlag(flag);
		return this;
	}
	
	public AdvancementBuilder setIcon(String icon)
	{
		this.icon = createIcon(Heads.getManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName()), icon);
		return this;
	}
	
	public AdvancementBuilder setIcon(ItemStack icon)
	{
		this.icon = icon;
		return this;
	}
	
	public AdvancementBuilder setCriteria(String[] values, String[][] requirement)
	{
		this.criteria = createCriteria(values, requirement);
		return this;
	}
	
	public AdvancementData build()
	{
		icon = icon != null ? icon : new ItemStack(Material.STONE);
		flag = flag != null ? flag : createFlag(null);
		frame = frame != null ? frame : createFrame(null);
		visibility = visibility != null ? visibility : createVisibility(null);
		display = display != null ? display : createDisplay();
		advancement = advancement != null ? advancement : createAdvancement();
		return new AdvancementData(stringAdvancement, advancement, icon, enabled, criteriaLookupMap);
	}
	
	public Advancement createAdvancement()
	{
		
		//HANDLE ADVANCEMENT CREATION IN A DIFFERENT WAY
		if(key == null)
		{
			return null;
		}
		advancement = new Advancement(parent, key, display, flag);
		advancement.setCriteria(criteria);
		return advancement;
	}
	
	private AdvancementDisplay createDisplay()
	{
		icon = icon != null ? icon : new ItemStack(Material.STONE);
		AdvancementDisplay display = new AdvancementDisplay(icon,
				stringAdvancement.getTitle(),
				stringAdvancement.getDescription(),
				frame,
				visibility);
		display.setBackgroundTexture(stringAdvancement.getTexture());
		display.setY((float) stringAdvancement.getX());
		display.setX((float) stringAdvancement.getY());
		return display;
	}
	
	private Criteria createCriteria(String[] values, String[][] requirement)
	{
		if(values == null || values.length == 0 || requirement == null || requirement.length == 0)
		{
			return null;
		}
		return new Criteria(values, requirement);
	}
	
	private ItemStack createIcon(Config config, String path)
	{
		if(path == null)
		{
			stringAdvancement.setIcon("STONE");
			return new ItemStack(Material.STONE);
		}
		MobHeadData mobHeadData = MobHeadDataUtility.getFirstValidConfigHeadData(config, path);
		if(mobHeadData != null)
		{
			stringAdvancement.setIcon(path);
			return mobHeadData.createHeadItem();
		}
		Material material;
		try
		{
			stringAdvancement.setIcon(path);
			material = Material.valueOf(path);
		} catch(IllegalArgumentException e)
		{
			stringAdvancement.setIcon("STONE");
			material = Material.STONE;
		}
		return new ItemStack(material);
	}
	
	private AdvancementFrame createFrame(String value)
	{
		if(value == null)
		{
			stringAdvancement.setFrame("TASK");
			return AdvancementFrame.TASK;
		}
		AdvancementFrame frame;
		try
		{
			stringAdvancement.setFrame(value);
			frame = AdvancementFrame.valueOf(value);
			
		} catch(IllegalArgumentException ignored)
		{
			stringAdvancement.setFrame("TASK");
			frame = AdvancementFrame.TASK;
		}
		return frame;
	}
	
	private AdvancementVisibility createVisibility(String value)
	{
		if(value == null)
		{
			stringAdvancement.setVisibility("VANILLA");
			return AdvancementVisibility.VANILLA;
		}
		AdvancementVisibility advancementVisibility = AdvancementVisibility.parseVisibility(value);
		stringAdvancement.setVisibility(advancementVisibility.getName());
		return advancementVisibility;
	}
	
	private AdvancementFlag[] createFlag(String value)
	{
		if(value == null)
		{
			stringAdvancement.setFlag("SHOW_TOAST");
			return new AdvancementFlag[]{AdvancementFlag.SHOW_TOAST};
		}
		if(value.equals("TOAST_AND_MESSAGE"))
		{
			stringAdvancement.setFlag("TOAST_AND_MESSAGE");
			return AdvancementFlag.TOAST_AND_MESSAGE;
		}
		try
		{
			stringAdvancement.setFlag(value);
			return new AdvancementFlag[]{AdvancementFlag.valueOf(value)};
		} catch(IllegalArgumentException ignore)
		{
			stringAdvancement.setFlag("SHOW_TOAST");
			return new AdvancementFlag[]{AdvancementFlag.SHOW_TOAST};
		}
	}
	
	@SafeVarargs
	private String[][] listsTo2DArray(List<String>... lists)
	{
		int numRows = lists.length;
		int numCols = lists[0].size();
		
		String[][] result = new String[numRows][numCols];
		
		for(int i = 0; i < numRows; i++)
		{
			List<String> currentList = lists[i];
			for(int j = 0; j < numCols; j++)
			{
				result[i][j] = currentList.get(j);
			}
		}
		
		return result;
	}
	
	private List<String>[] listToNestedArray(List<List<String>> nestedList)
	{
		List<String>[] array = new List[nestedList.size()];
		
		for(int i = 0; i < nestedList.size(); i++)
		{
			List<String> currentList = nestedList.get(i);
			array[i] = new ArrayList<>(currentList);
		}
		
		return array;
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
	
	public ItemStack getIcon()
	{
		return icon;
	}
	
	public AdvancementDisplay getDisplay()
	{
		return display;
	}
	
	public AdvancementVisibility getVisibility()
	{
		return visibility;
	}
	
	public AdvancementFrame getFrame()
	{
		return frame;
	}
	
	public Config getConfig()
	{
		return stringAdvancement.getConfig();
	}
	
	public Advancement getParent()
	{
		return parent;
	}
	
	public AdvancementFlag[] getFlag()
	{
		return flag;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public Map<String, String> getCriteriaLookupMap()
	{
		return criteriaLookupMap;
	}
	
	public StringAdvancement getStringAdvancement()
	{
		return stringAdvancement;
	}
}