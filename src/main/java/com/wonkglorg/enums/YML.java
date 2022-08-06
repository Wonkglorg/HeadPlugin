package com.wonkglorg.enums;

public enum YML
{
	CONFIG("config.yml"),
	ENGLISH("en-lang.yml"),
	HEAD_DATA("head_data.yml");
	private final String fileName;
	YML(String fileName){
	this.fileName = fileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	
}