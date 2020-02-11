package bank_account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Bank;
import bank_domain.CheckingAccount;
import bank_domain.Client;
import bank_domain.YoungAccount;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;
import bank_services.Services;

public class UpgradeMethodTest {
	private Bank bank;
	private Client youngClient;
	private YoungAccount young;
	private Services services;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		this.services = new Services();
		this.bank = new Bank("CGD");

		this.youngClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 17);

		this.young = (YoungAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0));
	}

	@Test
	public void success() throws BankException, AccountException, ClientException {
		this.young.deposit(19_000);

		this.youngClient.setAge(18);
		CheckingAccount checking = this.young.upgrade();

		assertEquals(1, this.bank.getTotalNumberOfAccounts());
		assertEquals(1, this.youngClient.getNumberOfAccounts());
		assertTrue(this.youngClient.hasAccount(checking));

		assertEquals(this.youngClient, checking.getClient());
		assertEquals(19102, checking.getBalance());
	}

	@Test
	public void successWith5Accounts() throws BankException, AccountException, ClientException {
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);

		this.youngClient.setAge(18);
		CheckingAccount checking = this.young.upgrade();

		assertEquals(5, this.bank.getTotalNumberOfAccounts());
		assertEquals(5, this.youngClient.getNumberOfAccounts());
		assertTrue(this.youngClient.hasAccount(checking));
		assertFalse(this.youngClient.hasAccount(this.young));

		assertEquals(this.youngClient, checking.getClient());
		assertEquals(100, checking.getBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}