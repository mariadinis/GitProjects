package bank_client;

import static org.junit.Assert.assertEquals;
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

public class HappyBirthdayMethodTest {
	private Bank bank;
	private Client youngClient;
	private YoungAccount young;
	private Services services;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		this.services = new Services();
		this.bank = new Bank("CGD");

		this.youngClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 16);

		this.young = (YoungAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0));
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
	}

	@Test
	public void successNoUpgrade() throws BankException, AccountException, ClientException {
		this.youngClient.happyBirthDay();

		assertEquals(17, this.youngClient.getAge());
		assertTrue(this.youngClient.getAccounts().allMatch(a -> a instanceof YoungAccount));
	}

	@Test
	public void successUpGrade() throws BankException, AccountException, ClientException {
		this.youngClient.happyBirthDay();
		this.youngClient.happyBirthDay();

		assertEquals(18, this.youngClient.getAge());
		assertTrue(this.youngClient.getAccounts().allMatch(a -> a instanceof CheckingAccount));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
