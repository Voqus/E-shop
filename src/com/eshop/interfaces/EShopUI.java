package com.eshop.interfaces;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.eshop.actions.ShopActionListener;
import com.eshop.actor.Client;
import com.eshop.database.DBManager;

@SuppressWarnings("serial")
public final class EShopUI extends JFrame
{
	public JList<String> productList = new JList<String>();
	public JList<String> categoryList;
	private ShopActionListener sal = new ShopActionListener(this);

	private JLabel userLabel = new JLabel();
	public JLabel cartLabel = new JLabel();
	public JLabel balanceLabel = new JLabel();
	public JLabel logoutLabel = new JLabel();

	public Client _userLogged;

	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	public EShopUI()
	{
	}

	public EShopUI(final Client userLogged)
	{
		_userLogged = userLogged;
		loadData();
		initializeGUI();
	}

	private void initializeGUI()
	{
		setTitle("E-Shop");
		setLayout(new BorderLayout());

		JPanel categoryPanel = new JPanel();
		categoryList.setVisibleRowCount(15);
		categoryList.setFixedCellWidth(150);
		categoryList.addMouseListener(sal);

		JScrollPane categoryPane = new JScrollPane(categoryList);
		categoryPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		categoryPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		categoryPanel.add(categoryPane);
		categoryPanel.setBorder(BorderFactory.createTitledBorder("Categories:"));

		JPanel logStatus = new JPanel();
		logStatus.setLayout(new FlowLayout());

		userLabel.setText("<html><b>Logged in as:</b><i> " + _userLogged.getName() + " " + _userLogged.getSurname() + "</i></html>");
		balanceLabel.setText("<html><b>Balance:</b> " + _userLogged.getBalance() + "</html>");
		cartLabel.setText("<html><a href='#'>My cart <a/></html>");
		cartLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cartLabel.addMouseListener(sal);
		logoutLabel.setText("<html><a href='#'>Log out<a/></html>");
		logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoutLabel.addMouseListener(sal);

		logStatus.add(userLabel);
		logStatus.add(balanceLabel);
		logStatus.add(cartLabel);
		logStatus.add(logoutLabel);

		JPanel focusedPanel = new JPanel();
		focusedPanel.setLayout(new FlowLayout());

		productList.setVisibleRowCount(19);
		productList.setFixedCellWidth(500);
		productList.addMouseListener(sal);
		JScrollPane productPane = new JScrollPane(productList);
		productList.setVisible(false);

		focusedPanel.add(productPane);
		focusedPanel.setBorder(BorderFactory.createTitledBorder("Products:"));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(logStatus, BorderLayout.NORTH);
		mainPanel.add(categoryPanel, BorderLayout.WEST);
		mainPanel.add(focusedPanel, BorderLayout.CENTER);

		add(mainPanel, BorderLayout.CENTER);
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void loadData()
	{
		DefaultListModel<String> model = new DefaultListModel<String>();
		_connection = DBManager.connect();
		try
		{
			_preparedStatement = _connection.prepareStatement("SELECT NAME FROM Categories");
			_resultSet = _preparedStatement.executeQuery();

			while (_resultSet.next())
			{
				model.addElement(_resultSet.getString("NAME"));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		categoryList = new JList<String>(model);
	}

}
