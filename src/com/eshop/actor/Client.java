package com.eshop.actor;

public final class Client extends User
{
	private float _balance;
	private int _a_pk;

	public Client(final String name, final String surname, String username, final String password, final float balance, final int a_pk)
	{
		super(name, surname, username, password);
		this._balance = balance;
		_a_pk = a_pk;
	}

	public float getBalance()
	{
		return _balance;
	}

	public void setBalance(float _balance)
	{
		this._balance = _balance;
	}

	public int getA_pk()
	{
		return _a_pk;
	}

	public void setA_pk(int a_pk)
	{
		this._a_pk = a_pk;
	}

}
