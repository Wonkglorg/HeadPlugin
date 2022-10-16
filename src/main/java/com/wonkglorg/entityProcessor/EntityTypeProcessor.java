package com.wonkglorg.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import static com.wonkglorg.utils.HeadUtils.dropHead;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;

public abstract class EntityTypeProcessor
{
	protected Entity entity;
	protected WeightedRandomPicker<MobHeadData> weightedRandomPicker;
	
	abstract String path();
	
	public abstract boolean matches(Entity entity);
	
	public abstract void process(Entity entity, Location loc);
	
	protected void processHeadData(Entity entity, Location loc)
	{
		this.entity = entity;
		String path = path();
		Config config = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DATA.getFileName());
		Config countConfig = Heads.getPluginManager().getConfigManager().getConfig(YML.HEAD_DROP_NUMBERS.getFileName());
		
		List<MobHeadData> mobHeadDataList = MobHeadData.getAllValidConfigHeadData(config, path);
		weightedRandomPicker = new WeightedRandomPicker<>();
		if(mobHeadDataList.isEmpty())
		{
			return;
		}
		for(MobHeadData mobHead : mobHeadDataList)
		{
			if(mobHead.getDropChance() > 0.0 && mobHead.isEnabled())
			{
				weightedRandomPicker.addEntry(mobHead, mobHead.getDropChance());
			}
		}
		
		if(weightedRandomPicker.getEntries().size() > 1)
		{
			if(weightedRandomPicker.getAccumulatedWeight() <= 100)
			{
				for(MobHeadData mobHead : mobHeadDataList)
				{
					if(mobHead.getDropChance() > Math.random() * 100)
					{
						dropHead(mobHead.getTexture(), mobHead.getName(), mobHead.getDescription(), loc);
						countConfig.set(path, countConfig.contains(path) ? countConfig.getInt(path)+1 : 1);
						countConfig.silentSave();
						return;
					}
				}
			}
			MobHeadData picked = weightedRandomPicker.getRandom();
			dropHead(picked.getTexture(), picked.getName(), picked.getDescription(), loc);
		}
		
		if(weightedRandomPicker.getEntries().size() == 1)
		{
			if(weightedRandomPicker.getAccumulatedWeight() > Math.random() * 100)
			{
				MobHeadData mobHead = mobHeadDataList.get(0);
				dropHead(mobHead.getTexture(), mobHead.getName(), mobHead.getDescription(), loc);
				countConfig.set(path, countConfig.contains(path) ? countConfig.getInt(path)+1 : 1);
				countConfig.silentSave();
			}
		}
	}
}