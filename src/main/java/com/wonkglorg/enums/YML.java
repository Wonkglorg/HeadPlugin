package com.wonkglorg.enums;

public enum YML
{
	CONFIG("config.yml"),
	HEAD_DROP_NUMBERS("head-drop-info.yml"),
	HEAD_DATA("head_data.yml"),
	HEAD_DATA_BACKUP("head_data_backup.yml"),
	
	
	
	;
	private final String fileName;
	YML(String fileName){
	this.fileName = fileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	
}