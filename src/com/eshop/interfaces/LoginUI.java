package com.eshop.interfaces;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.eshop.actions.LoginButtonListener;

@SuppressWarnings("serial")
public final class LoginUI extends JFrame
{
	public JTextField usernameField;
	public JPasswordField passwordField;
	public JButton connectButton;
	public JButton cancelButton;

	public LoginUI()
	{

	}

	public LoginUI(final String title)
	{
		super(title);
		initializeGUI();
	}

	private void initializeGUI()
	{
		setLayout(new FlowLayout());
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4, 2));

		usernameField = new JTextField(15);
		passwordField = new JPasswordField(15);

		connectButton = new JButton("Log in");
		cancelButton = new JButton("Cancel");

		LoginButtonListener buttonListener = new LoginButtonListener(this);
		connectButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);

		mainPanel.add(new JLabel("Username: "));
		mainPanel.add(usernameField);

		mainPanel.add(new JLabel("Password: "));
		mainPanel.add(passwordField);

		mainPanel.add(connectButton);
		mainPanel.add(cancelButton);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Login Form"));

		add(mainPanel);
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}

}
