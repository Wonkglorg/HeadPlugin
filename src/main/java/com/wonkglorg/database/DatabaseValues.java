package com.wonkglorg.database;

public enum DatabaseValues
{
	TABLE_HEAD_TYPES("HeadTypes"),
	TABLE_HEAD_VALUES("HeadValues"),
	
	;
	
	private final String name;
	
	DatabaseValues(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}