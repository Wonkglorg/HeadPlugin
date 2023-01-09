package com.wonkglorg.enums;

public enum WonkyHeadValues
{
	PLUGIN("WonkyHeads"),
	
	
	;
	private final String key;
	WonkyHeadValues(String key)
	{
		this.key = key;
	}
	
	public String getKey()
	{
		return key;
	}
}