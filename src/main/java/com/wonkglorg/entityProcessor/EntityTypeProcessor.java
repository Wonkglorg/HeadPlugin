package com.wonkglorg.entityProcessor;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.YML;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.ConfigManager;
import com.wonkglorg.utilitylib.utils.random.WeightedRandomPicker;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;

public abstract class EntityTypeProcessor
{
	
	protected Entity entity;
	private final ConfigManager configManager = Heads.getPluginManager().getConfigManager();
	
	abstract String path();
	
	public abstract boolean matches(Entity entity);
	
	public abstract void process(Entity entity, Location loc);
	
	protected void processHeadData(Entity entity, Location loc)
	{
		this.entity = entity;
		String path = path();
		Config config = configManager.getConfig(YML.HEAD_DATA.getFileName());
		Config countConfig = configManager.getConfig(YML.HEAD_DROP_NUMBERS.getFileName());
		
		List<MobHeadData> mobHeadDataList = MobHeadData.getAllValidConfigHeadData(config, path);
		
		MobHeadData mobHead = MobHeadData.randomHeadDrop(mobHeadDataList);
		
		if(mobHead == null)
		{
			return;
		}
		
		MobHeadData.dropHead(mobHead.getTexture(), mobHead.getName(), mobHead.getDescription(), loc);
		countConfig.set(path, countConfig.contains(path) ? countConfig.getInt(path) + 1 : 1);
		countConfig.silentSave();
	}
}