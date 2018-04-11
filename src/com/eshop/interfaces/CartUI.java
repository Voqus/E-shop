package com.eshop.interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.eshop.actor.Client;
import com.eshop.database.DBManager;

@SuppressWarnings("serial")
public final class CartUI extends JFrame implements WindowListener
{
	private static volatile CartUI instance = null;
	private static JTable _cartTable = new JTable()
	{
		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	};

	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ResultSet _resultSet;

	private Client _userLogged;

	private CartUI(final Client userLogged)
	{
		_userLogged = userLogged;
		loadTableData();
		initializeGUI();
	}

	public static CartUI getInstance(final Client userLogged)
	{
		if (instance == null)
		{
			synchronized (CartUI.class)
			{
				if (instance == null)
				{
					instance = new CartUI(userLogged);
				}
			}
		}
		return instance;
	}

	private void initializeGUI()
	{
		setTitle("My Cart");
		setLayout(new BorderLayout());
		JTableHeader tableHeader = _cartTable.getTableHeader();
		_cartTable.setRowHeight(24);
		_cartTable.getColumn("NAME").setPreferredWidth(250);
		JScrollPane scrollPane = new JScrollPane(_cartTable);
		add(tableHeader, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		pack();
		addWindowListener(this);
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void loadTableData()
	{
		_connection = DBManager.connect();
		try
		{
			_preparedStatement = _connection.prepareStatement("SELECT Products.NAME,Products.PRICE FROM Cart NATURAL JOIN Products WHERE A_PK=?");
			_preparedStatement.setInt(1, _userLogged.getA_pk());
			_resultSet = _preparedStatement.executeQuery();
			_cartTable.setModel(resultSetToTableModel(_resultSet));
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
	}

	public static TableModel resultSetToTableModel(ResultSet rs)
	{
		try
		{
			ResultSetMetaData metaData = rs.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			Vector<String> columnNames = new Vector<String>();

			// Get the column names
			for (int column = 0; column < numberOfColumns; column++)
			{
				columnNames.addElement(metaData.getColumnLabel(column + 1));
			}

			// Get all rows.
			Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

			while (rs.next())
			{
				Vector<Object> newRow = new Vector<Object>();

				for (int i = 1; i <= numberOfColumns; i++)
				{
					newRow.addElement(rs.getObject(i));
				}

				rows.addElement(newRow);
			}

			return new DefaultTableModel(rows, columnNames);
		} catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		instance = null;
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{

	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
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
