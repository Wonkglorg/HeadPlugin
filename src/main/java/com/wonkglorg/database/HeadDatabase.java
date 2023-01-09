package com.wonkglorg.database;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.logger.Logger;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class HeadDatabase
{
	
	protected static Connection connection;
	protected final String DATABASE_NAME;
	protected final String DATABASE_PATH;
	
	private final Map<DatabaseValues, String> tableMap = new HashMap<>();
	
	public HeadDatabase(Path path, String name)
	{
		if(name == null || path == null)
		{
			throw new RuntimeException();
		}
		DATABASE_NAME = name.endsWith(".db") ? name : name + ".db";
		DATABASE_PATH = path.toAbsolutePath().toString();
		connect();
	}
	
	public void createTable(String command)
	{
		connect();
		try(Statement statement = connection.createStatement())
		{
			statement.setQueryTimeout(30);
			statement.executeUpdate(command);
		} catch(SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	//world and permission are both seperated with , if multiple are selected.
	
	//once a path is selected, add all into a list and then go from there, do not reaquire all heads on each action unless it is the final one
	
	private void createTables() throws SQLException
	{
		initializeTableMap();
		
		connect();
		
		tableMap.forEach((databaseValues, s) ->
		{
			try(PreparedStatement preparedStatement = connection.prepareStatement(s))
			{
				
				preparedStatement.execute();
				
			} catch(SQLException e)
			{
				throw new RuntimeException(e);
			}
			
		});
		
		String sql = "CREATE INDEX IF NOT EXISTS " + DatabaseValues.TABLE_HEAD_VALUES.getName() + "path_index ON path";
		try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
		{
			
			preparedStatement.execute();
		}
		
		updateHeadTypeTable();
	}
	
	private void updateHeadTypeTable()
	{
		
		//INSERT OR IGNORE INTO
		
		//use insert or ignore on both primary keys to add them or ignore em if they already exist
		
		// including 1 table with all default values for head textures etc
		//include a second table with all default head paths, if any do not exist copy the head path to the new table and copy default textures to new table as well
	}
	
	private void initializeTableMap()
	{
		if(tableMap.isEmpty())
		{
			return;
		}
		String sql = "CREATE TABLE IF NOT EXISTS " +
					 DatabaseValues.TABLE_HEAD_TYPES.getName() +
					 "(path STRING PRIMARY KEY, mob_type STRING NOT NULL)";
		
		tableMap.put(DatabaseValues.TABLE_HEAD_TYPES, sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " +
			  DatabaseValues.TABLE_HEAD_VALUES.getName() +
			  "(path STRING PRIMARY KEY, file_name STRING PRIMARY KEY, name STRING NOT NULL, description STRING NOT NULL, texture STRING NOT NULL, enabled BOOLEAN, drop_chance DOUBLE, worlds STRING, permission_requirement STRING, sound_field STRING )";
		
		tableMap.put(DatabaseValues.TABLE_HEAD_VALUES, sql);
	}
	
	public void connect()
	{
		if(connection != null)
		{
			return;
		}
		try
		{
			Class.forName("org.sqlite.JDBC");
			new File(String.valueOf(DATABASE_PATH)).mkdir();
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH + File.separator + DATABASE_NAME);
		} catch(ClassNotFoundException | SQLException e)
		{
			Logger.logFatal(Heads.getInstance(), e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public void disconnect() throws SQLException
	{
		if(connection != null)
		{
			connection.close();
		}
	}
	
	public Connection getConnection()
	{
		connect();
		return connection;
	}
	
}