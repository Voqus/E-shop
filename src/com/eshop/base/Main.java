package com.eshop.base;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.eshop.database.DBManager;
import com.eshop.interfaces.LoginUI;

public class Main
{
	public static void main(String... args)
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		DBManager.setDbPath("C://users//laptop////workspace//Github//E-shop//res//EshopDatabase.sqlite");
		SwingUtilities.invokeLater(() ->
		{
			new LoginUI("E-Shop");
		});
	}
}
