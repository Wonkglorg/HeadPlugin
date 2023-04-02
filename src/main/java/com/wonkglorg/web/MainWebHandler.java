package com.wonkglorg.web;

import io.javalin.Javalin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MainWebHandler
{
	
	public MainWebHandler()
	{
		Javalin app = Javalin.create(config -> config.staticFiles.add("/public")).start(7070);
		
		app.get("/", ctx ->
		{
			System.out.println("-----------------------------------------");
			System.out.println("Got connection");
			String htmlString;
			try(InputStream inputStream = getClass().getResourceAsStream("/public/html/main.html"))
			{
				htmlString = new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
			}
			
			ctx.html(htmlString);
		});
	}
}
