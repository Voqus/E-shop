package com.eshop.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import com.eshop.database.DBManager;
import com.eshop.interfaces.CartUI;
import com.eshop.interfaces.EShopUI;
import com.eshop.interfaces.LoginUI;
import com.eshop.interfaces.ViewProduct;

public final class ShopActionListener implements MouseListener
{
	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	private EShopUI _shop;
	private DefaultListModel<String> _productListModel = new DefaultListModel<String>();

	public ShopActionListener(EShopUI shop)
	{
		_shop = shop;
	}

	@Override
	public void mouseClicked(MouseEvent me)
	{
		if (me.getSource().equals(_shop.categoryList))
		{
			if (me.getClickCount() == 1)
			{
				if (!_productListModel.isEmpty())
					_productListModel.removeAllElements();
				_shop.productList.removeAll();

				System.out.println("Opening products for category: " + _shop.categoryList.getSelectedValue());
				_connection = DBManager.connect();
				try
				{
					_preparedStatement = _connection.prepareStatement("SELECT Products.* FROM Products,Categories,Category_Products WHERE Categories.NAME=? AND Categories.C_PK=Category_Products.C_PK AND Category_Products.P_PK=Products.P_PK");
					_preparedStatement.setString(1, _shop.categoryList.getSelectedValue());
					_resultSet = _preparedStatement.executeQuery();

					while (_resultSet.next())
					{
						System.out.println("FOUND");
						_productListModel.addElement(_resultSet.getString("name"));
					}

					_shop.productList.setVisible(true);
					_shop.productList.setModel(_productListModel);
				} catch (SQLException e)
				{
					e.printStackTrace();
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
				System.out.println(_shop.productList.getModel().getSize());
			}
		}
		if (me.getSource().equals(_shop.productList))
		{
			if (me.getClickCount() == 2)
			{
				System.out.println("Double clicked on index: " + _shop.productList.getSelectedValue());
				SwingUtilities.invokeLater(() ->
				{
					ViewProduct.getInstance(_shop, _shop._userLogged);
				});
			}
		}
		if (me.getSource().equals(_shop.logoutLabel))
		{
			if (me.getClickCount() == 1)
			{
				_shop.dispose();
				SwingUtilities.invokeLater(() ->
				{
					// Database path is already defined at load time, no need to re-define it
						new LoginUI("E-Shop");
					});
			}
		}
		if (me.getSource().equals(_shop.cartLabel))
		{
			if (me.getClickCount() == 1)
			{
				SwingUtilities.invokeLater(() ->
				{
					CartUI.getInstance(_shop._userLogged);
				});
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent me)
	{

	}

	@Override
	public void mouseExited(MouseEvent me)
	{

	}

	@Override
	public void mousePressed(MouseEvent me)
	{

	}

	@Override
	public void mouseReleased(MouseEvent me)
	{

	}

}
