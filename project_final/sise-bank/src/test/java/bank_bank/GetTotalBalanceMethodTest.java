package bank_bank;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Bank;
import bank_domain.Client;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;

public class GetTotalBalanceMethodTest {
	private Bank bank;
	private Client client;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.bank = new Bank("CGD");

		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void fiveAccounts() throws BankException, AccountException, ClientException {
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);

		assertEquals(500, this.bank.getTotalBalance());
	}

	@Test
	public void noAccounts() {
		assertEquals(0, this.bank.getTotalBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
