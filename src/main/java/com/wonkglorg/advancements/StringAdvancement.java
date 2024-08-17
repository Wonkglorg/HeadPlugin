package com.wonkglorg.advancements;

import com.wonkglorg.utilitylib.manager.config.Config;
import org.jetbrains.annotations.NotNull;

public class StringAdvancement
{
	private final String key;
	private final Config config;
	private String path;
	private String icon;
	private String flag;
	private String title;
	private String description;
	private String visibility;
	private String parent;
	private String frame;
	private String texture;
	private double x;
	private double y;
	
	public StringAdvancement(String key, String path, Config config)
	{
		this.key = key;
		this.path = path;
		this.config = config;
	}
	
	public StringAdvancement(String key,
							 Config config,
							 String path,
							 String flag,
							 String title,
							 String description,
							 String visibility,
							 String parent,
							 String frame,
							 String texture,
							 String icon,
							 double x,
							 double y)
	{
		this.key = key;
		this.config = config;
		this.path = path;
		this.flag = flag;
		this.title = title;
		this.description = description;
		this.visibility = visibility;
		this.parent = parent;
		this.frame = frame;
		this.texture = texture;
		this.x = x;
		this.y = y;
	}
	
	public String getIcon()
	{
		return icon;
	}
	
	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	
	public String getFrame()
	{
		return frame;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public void setFrame(String frame)
	{
		this.frame = frame;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getFlag()
	{
		return flag;
	}
	
	public Config getConfig()
	{
		return config;
	}
	
	public void setFlag(@NotNull String flag)
	{
		this.flag = flag;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title != null ? title : " ";
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description != null ? description : " ";
	}
	
	public String getVisibility()
	{
		return visibility;
	}
	
	public void setVisibility(@NotNull String visibility)
	{
		this.visibility = visibility;
	}
	
	public String getParent()
	{
		return parent;
	}
	
	public void setParent(@NotNull String parent)
	{
		this.parent = parent;
	}
}