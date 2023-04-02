package com.wonkglorg.web.enums;

public enum WebPaths
{
	HTMLMainPage("public/html/main.html"),
	;
	
	private final String path;
	
	WebPaths(String path)
	{
		this.path = path;
	}
	
	public String getResourcePath()
	{
		return "resources/" + path;
		
	}
	
	public String getPath()
	{
		return path;
	}
}
