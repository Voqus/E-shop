package com.eshop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager
{
	private static String DB_PATH = null;

	public static Connection connect()
	{
		if (DB_PATH == null)
		{
			try
			{
				throw new Exception("You must specify your SQLite database path");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public static void setDbPath(final String path)
	{
		DB_PATH = path;
	}
}
