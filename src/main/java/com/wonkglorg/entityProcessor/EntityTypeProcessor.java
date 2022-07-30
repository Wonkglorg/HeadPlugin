package com.wonkglorg.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import com.wonkglorg.utils.HeadUtils;
import static com.wonkglorg.utils.HeadUtils.dropHead;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Set;

public abstract class EntityTypeProcessor
{
	protected Entity entity;
	
	abstract String path();
	
	public abstract boolean matches(Entity entity);
	
	public abstract void process(Entity entity, Location loc);
	
	protected void processHeadData(Entity entity, Location loc)
	{
		this.entity = entity;
		String path = path();
		Config config = Heads.getManager().getConfig(YML.HEAD_DATA.getFileName());
		Set<String> subHeads = config.getSection(path,false);
		if(!subHeads.isEmpty())
		{
			WeightedRandomPicker<String> weightedRandomPicker = new WeightedRandomPicker<>();
			String finalPath = path;
			subHeads.forEach(s ->
			{
				double dropchance = config.getDouble(finalPath + "." + s + ".DropChance");
				if(dropchance != 0.0)
				{
					weightedRandomPicker.addEntry(s, dropchance);
				}
				
			});
			if(!weightedRandomPicker.getEntries().isEmpty())
			{
				String picked = weightedRandomPicker.getRandom();
				path = path + "." + picked;
			}
		}
		if(HeadUtils.readConfigBoolean(YML.HEAD_DATA, path + ".Enabled"))
		{
			dropHead(path + ".Texture", path + ".Name", path + ".Description", loc);
		}
		
	}
	
}