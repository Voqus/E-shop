package com.eshop.interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.eshop.actions.ViewProductActionListener;
import com.eshop.actor.Client;
import com.eshop.database.DBManager;

@SuppressWarnings("serial")
public final class ViewProduct extends JFrame implements WindowListener
{
	private static volatile ViewProduct instance = null;
	public JButton purchaseButton;
	public JButton closeButton;

	private EShopUI _shop;
	public Client _userLogged;
	private ViewProductActionListener productListener;

	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	public ViewProduct()
	{

	}

	private ViewProduct(final EShopUI shop, final Client userLogged)
	{
		_shop = shop;
		productListener = new ViewProductActionListener(this, _shop);
		_userLogged = userLogged;
		initializeGUI();
	}

	public static ViewProduct getInstance(final EShopUI shop, Client userLogged)
	{
		if (instance == null)
		{
			synchronized (ViewProduct.class)
			{
				if (instance == null)
				{
					instance = new ViewProduct(shop, userLogged);
				}
			}
		}
		return instance;
	}

	private void initializeGUI()
	{
		setTitle(String.valueOf(_shop.productList.getSelectedValue()));
		setLayout(new BorderLayout());
		BufferedImage img = null;
		JLabel descLabel = new JLabel();
		JLabel priceLabel = new JLabel();
		try
		{
			_connection = DBManager.connect();
			_preparedStatement = _connection.prepareStatement("SELECT * FROM Products WHERE NAME=?");
			_preparedStatement.setString(1, String.valueOf(_shop.productList.getSelectedValue()));
			_resultSet = _preparedStatement.executeQuery();
			if (_resultSet.next())
			{
				img = ImageIO.read(new URL(_resultSet.getString("icon")));
				descLabel.setText(_resultSet.getString("DESC"));
				priceLabel.setText("Price: " + _resultSet.getFloat("price"));
			} else
			{
				return;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (SQLException sql)
		{
			sql.printStackTrace();
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
			} catch (SQLException sql2)
			{
				sql2.printStackTrace();
			}
		}
		ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_SMOOTH));
		JLabel imageLabel = new JLabel(imageIcon, JLabel.CENTER);
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout());
		displayPanel.add(imageLabel, BorderLayout.CENTER);

		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(descLabel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Description: "));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setPreferredSize(new Dimension(300, 120));
		priceLabel.setBorder(BorderFactory.createTitledBorder("Price: "));
		informationPanel.add(scrollPane, BorderLayout.CENTER);
		informationPanel.add(priceLabel, BorderLayout.SOUTH);
		informationPanel.setBorder(BorderFactory.createTitledBorder("Information: "));
		displayPanel.add(informationPanel, BorderLayout.SOUTH);
		displayPanel.setBorder(BorderFactory.createTitledBorder(""));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		purchaseButton = new JButton("Add to cart");
		purchaseButton.addActionListener(productListener);
		closeButton = new JButton("Close");
		closeButton.addActionListener(productListener);
		buttonPanel.add(purchaseButton);
		buttonPanel.add(closeButton);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);
		addWindowListener(this);
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent we)
	{
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		instance = null;
	}

	@Override
	public void windowClosing(WindowEvent we)
	{

	}

	@Override
	public void windowDeactivated(WindowEvent we)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent we)
	{
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
	}

}
