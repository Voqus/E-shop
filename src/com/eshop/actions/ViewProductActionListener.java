package com.eshop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.eshop.database.DBManager;
import com.eshop.interfaces.EShopUI;
import com.eshop.interfaces.ViewProduct;

public final class ViewProductActionListener implements ActionListener
{
	private ViewProduct _viewProduct;
	private EShopUI _shop;

	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	public ViewProductActionListener(final ViewProduct viewProduct, final EShopUI shop)
	{
		_viewProduct = viewProduct;
		_shop = shop;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(_viewProduct.purchaseButton))
		{
			_connection = DBManager.connect();
			float balance = 0;
			int ppk = 0;

			try
			{
				_preparedStatement = _connection.prepareStatement("SELECT * FROM Products WHERE NAME=?");
				_preparedStatement.setString(1, _shop.productList.getSelectedValue());

				_resultSet = _preparedStatement.executeQuery();

				if (_resultSet.next())
				{
					balance = _resultSet.getFloat("price");
					ppk = _resultSet.getInt("p_pk");
				} else
				{
					JOptionPane.showMessageDialog(null, "Not found");
				}

				float afterPrice = _viewProduct._userLogged.getBalance() - balance;
				if (afterPrice < 0)
				{
					JOptionPane.showMessageDialog(_viewProduct, "You do not have enough money to buy this item.", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				} else
				{
					_viewProduct._userLogged.setBalance(afterPrice);
					try
					{
						_preparedStatement = _connection.prepareStatement("UPDATE Accounts SET money=? WHERE a_pk=?");
						_preparedStatement.setFloat(1, afterPrice);
						_preparedStatement.setInt(2, _viewProduct._userLogged.getA_pk());
						_preparedStatement.executeUpdate();
					} catch (SQLException e2)
					{
						e2.printStackTrace();
					}

					try
					{
						_preparedStatement = _connection.prepareStatement("INSERT INTO Cart(A_PK,P_PK) VALUES (?,?)");
						_preparedStatement.setString(1, String.valueOf(_viewProduct._userLogged.getA_pk()));
						_preparedStatement.setInt(2, ppk);
						_preparedStatement.executeUpdate();
					} catch (SQLException e2)
					{
						e2.printStackTrace();
					}
				}
			} catch (SQLException e2)
			{
				e2.printStackTrace();
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
				} catch (SQLException sqle)
				{
					sqle.printStackTrace();
				}
			}
			_shop.balanceLabel.setText("<html><b>Balance:</b> " + _viewProduct._userLogged.getBalance() + "</html>");
			JOptionPane.showMessageDialog(_viewProduct, "The product has been added to your cart", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
		}

		if (e.getSource().equals(_viewProduct.closeButton))
		{
			_viewProduct.dispose();
		}
	}
}
