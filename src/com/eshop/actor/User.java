package com.eshop.actor;

public class User
{

	protected String _name;
	protected String _surname;
	protected String _username;
	protected String _password;

	public User(final String name, final String surname, final String username, final String password)
	{
		this._name = name;
		this._surname = surname;
		this._username = username;
		this._password = password;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String _name)
	{
		this._name = _name;
	}

	public String getSurname()
	{
		return _surname;
	}

	public void setSurname(String _surname)
	{
		this._surname = _surname;
	}

	public String getUsername()
	{
		return _username;
	}

	public void setUsername(String _username)
	{
		this._username = _username;
	}

	public String getPassword()
	{
		return _password;
	}

	public void setPassword(String _password)
	{
		this._password = _password;
	}
}
