package com.wonkglorg.enums;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.message.Message;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public enum English
{
	RELOAD_CONFIG_SUCCESS("reload-config-success", "<prefix>Successfully reloaded all configs!"),
	RELOAD_CONFIG_ERROR("reload-config-error", "<prefix>Successfully reloaded <config>!"),
	RELOAD_CONFIG_ALL_SUCCESS("reload-config-all-success", "<prefix><config> does not exist!"),
	COMMAND_HEAD_DROP_SUCCESS("command-head-drop-success", "<prefix>&#FFFF55Dropped &#fcca03<headcount> &#FFFF55Head"),
	PVP_HEAD_DESCRIPTION("pvp-head-description", "Killed by <killer>");
	
	private final String path;
	private final String message;
	private static final String prefix = "&#777b99[Heads] &#f78036";
	private static Config LANG;
	
	English(String path, String message)
	{
		this.message = message;
		this.path = path;
		setFile(Heads.getManager().getConfig(YML.ENGLISH.getFileName()));
	}
	
	public static void setFile(Config langConfig)
	{
		LANG = langConfig;
	}
	
	@Override
	public String toString()
	{
		if(LANG.contains(this.path))
		{
			return Objects.requireNonNull(LANG.getString(this.path)).replace("<prefix>", prefix);
		}
		return this.getMessage().replace("<prefix>", prefix);
	}
	
	public Component toComponent()
	{
		if(LANG.contains(this.path))
		{
			return Message.color(Objects.requireNonNull(LANG.getString(this.path)).replace("<prefix>", prefix));
		}
		return Message.color(this.getMessage().replace("<prefix>", prefix));
	}
	
	public String getPath()
	{
		return path;
	}
	
	private String getMessage()
	{
		return message;
	}
	
	public String getRawMessage()
	{
		return message;
	}
}