package com.wonkglorg.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import static com.wonkglorg.utils.HeadUtils.dropHead;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class EntityTypeProcessor
{
	protected Entity entity;
	private final List<String> chanceList = new ArrayList<>();
	protected WeightedRandomPicker<String> weightedRandomPicker;
	
	abstract String path();
	
	public abstract boolean matches(Entity entity);
	
	public abstract void process(Entity entity, Location loc);
	
	protected void processHeadData(Entity entity, Location loc)
	{
		this.entity = entity;
		String path = path();
		Config config = Heads.getManager().getConfig(YML.HEAD_DATA.getFileName());
		Set<String> subHeads = config.getSection(path, false);
		if(!subHeads.isEmpty())
		{
			weightedRandomPicker = new WeightedRandomPicker<>();
			String finalPath = path;
			subHeads.forEach(s ->
			{
				double dropchance = config.getDouble(finalPath + "." + s + ".DropChance");
				if(dropchance != 0.0)
				{
					if(config.getBoolean(finalPath + "." + s + ".Enabled"))
					{
						weightedRandomPicker.addEntry(s, dropchance);
						chanceList.add(s);
					}
				}
			});
			if(!weightedRandomPicker.getEntries().isEmpty())
			{
				if(weightedRandomPicker.getAccumulatedWeight() <= 100)
				{
					for(String path1 : chanceList)
					{
						if(config.getDouble(path + "." + path1 + ".DropChance") > Math.random() * 100)
						{
							dropHead(path + "." + path1 + ".Texture", path + "." + path1 + ".Name", path + "." + path1 + ".Description", loc);
							return;
						}
					}
					return;
				}
				if(weightedRandomPicker.getEntries().size() > 1)
				{
					String picked = weightedRandomPicker.getRandom();
					path = path + "." + picked;
				}
			} else
			{
				if(config.getBoolean(path + ".Enabled"))
				{
					weightedRandomPicker.addEntry(path, config.getDouble(path + ".DropChance"));
					
					if(weightedRandomPicker.getAccumulatedWeight() > Math.random() * 100)
					{
						dropHead(path + ".Texture", path + ".Name", path + ".Description", loc);
						return;
					}
				}
			}
			if(weightedRandomPicker.getEntries().isEmpty())
			{
				return;
			}
		}
		if(weightedRandomPicker.getEntries().size() != 1)
		{
			dropHead(path + ".Texture", path + ".Name", path + ".Description", loc);
		}
		
	}
	
}