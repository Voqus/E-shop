package com.eshop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.eshop.actor.Client;
import com.eshop.database.DBManager;
import com.eshop.interfaces.EShopUI;
import com.eshop.interfaces.LoginUI;

public final class LoginButtonListener implements ActionListener
{
	private static LoginUI _loginFrame;

	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	private static Client userLogged;

	public LoginButtonListener(final LoginUI loginFrame)
	{
		_loginFrame = loginFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(_loginFrame.connectButton))
		{
			String username = _loginFrame.usernameField.getText();
			String password = new String(_loginFrame.passwordField.getPassword());

			if (username.isEmpty() || password.isEmpty())
			{
				JOptionPane.showMessageDialog(null, "You cannot have empty fields in your login form.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try
			{
				_connection = DBManager.connect();
				_preparedStatement = _connection.prepareStatement("SELECT * FROM Accounts WHERE username=? AND password=?");
				_preparedStatement.setString(1, username);
				_preparedStatement.setString(2, password);
				_resultSet = _preparedStatement.executeQuery();

				if (_resultSet.next())
				{
					String name = _resultSet.getString("name");
					String surname = _resultSet.getString("surname");
					float balance = Float.parseFloat(_resultSet.getString("money"));
					int a_pk = Integer.parseInt(_resultSet.getString("a_pk"));
					userLogged = new Client(name, surname, username, password, balance, a_pk);
					SwingUtilities.invokeLater(() ->
					{
						new EShopUI(userLogged);
					});
					_loginFrame.dispose();
				} else
				{
					JOptionPane.showMessageDialog(null, "Incorrect account/password data", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

			} catch (SQLException e1)
			{
				e1.printStackTrace();
			} finally
			{
				try
				{
					if (_resultSet != null)
						_resultSet.close();
					if (_preparedStatement != null)
						_preparedStatement.close();
					if (_connection != null)
						_connection.close();

				} catch (SQLException e2)
				{
					e2.printStackTrace();
				}
			}
		}
		if (e.getSource().equals(_loginFrame.cancelButton))
		{
			System.exit(0);
		}
	}
}
