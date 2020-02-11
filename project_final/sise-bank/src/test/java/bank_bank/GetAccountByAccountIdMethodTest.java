package bank_bank;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Account;
import bank_domain.Bank;
import bank_domain.Client;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;

public class GetAccountByAccountIdMethodTest {
	private Bank bank;
	private Client client;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.bank = new Bank("CGD");

		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void success() throws BankException, AccountException, ClientException {
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		Account account = this.bank.getAccounts().findFirst().get();

		assertEquals(account, this.bank.getAccountByAccountId(account.getAccountId()));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
