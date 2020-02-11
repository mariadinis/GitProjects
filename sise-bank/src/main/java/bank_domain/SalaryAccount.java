package bank_domain;

import bank_domain.Bank.AccountType;
import bank_exceptions.AccountException;
import bank_exceptions.ClientException;

public class SalaryAccount extends Account {
	private final int salary;

	public SalaryAccount(Client client, int amount, int salary) throws AccountException, ClientException {
		super(client, amount);
		this.salary = salary;
	}

	@Override
	protected String getNextAcccountId() {
		return AccountType.SALARY.getPrefix() + Integer.toString(++counter);
	}

	@Override
	public void withdraw(int amount) throws AccountException {
		if (getBalance() - amount + this.getSalary() < 0) {
			throw new AccountException();
		}

		super.withdraw(amount);
	}

	public int getSalary() {
		return this.salary;
	}
}
