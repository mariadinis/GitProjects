package bank_domain;

import bank_domain.Bank.AccountType;
import bank_exceptions.AccountException;
import bank_exceptions.ClientException;

public class CheckingAccount extends Account {

	public CheckingAccount(Client client, int amount) throws AccountException, ClientException {
		super(client, amount);
	}

	@Override
	protected String getNextAcccountId() {
		return AccountType.CHECKING.getPrefix() + Integer.toString(++counter);
	}

	@Override
	public void withdraw(int amount) throws AccountException {
		if (amount > getBalance()) {
			throw new AccountException();
		}

		super.withdraw(amount);
	}

}
