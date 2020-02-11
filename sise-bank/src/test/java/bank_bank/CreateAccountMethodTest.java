package bank_bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Account;
import bank_domain.Bank;
import bank_domain.CheckingAccount;
import bank_domain.Client;
import bank_domain.SalaryAccount;
import bank_domain.SavingsAccount;
import bank_domain.Bank.AccountType;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;
import bank_services.Services;

public class CreateAccountMethodTest {
	private Bank bank;
	private Client client;
	private Services services;

	@Before
	public void setUp() throws BankException, ClientException {
		this.services = new Services();
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void successCheckingAccount() throws BankException, AccountException, ClientException {
		String iban = this.bank.createAccount(AccountType.CHECKING, this.client, 100, 0);

		Account account = this.services.getAccountByIban(iban);

		assertTrue(account instanceof CheckingAccount);
		assertEquals(1, this.bank.getTotalNumberOfAccounts());
		assertEquals(this.client, account.getClient());
		assertEquals(100, account.getBalance());
	}

	@Test
	public void successSavingsAccount() throws BankException, AccountException, ClientException {
		String iban = this.bank.createAccount(AccountType.SAVINGS, this.client, 100, 100);

		Account account = this.services.getAccountByIban(iban);

		assertTrue(account instanceof SavingsAccount);
		assertEquals(1, this.bank.getTotalNumberOfAccounts());
		assertEquals(this.client, account.getClient());
		assertEquals(100, account.getBalance());
		assertEquals(100, ((SavingsAccount) account).getBase());
	}

	@Test
	public void successYoungAccount() throws BankException, AccountException, ClientException {
		Client youngClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 16);
		String iban = this.bank.createAccount(AccountType.YOUNG, youngClient, 100, 100);

		Account account = this.services.getAccountByIban(iban);

		assertTrue(account instanceof SavingsAccount);
		assertEquals(1, this.bank.getTotalNumberOfAccounts());
		assertEquals(youngClient, account.getClient());
		assertEquals(100, account.getBalance());
		assertEquals(10, ((SavingsAccount) account).getBase());
	}

	@Test
	public void failYoungAccountMoreThan17() throws BankException, AccountException, ClientException {
		Client notSoYoungClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 18);

		try {
			this.bank.createAccount(AccountType.YOUNG, notSoYoungClient, 100, 100);
			fail();
		} catch (AccountException e) {
			assertEquals(0, this.bank.getTotalNumberOfAccounts());
		}
	}

	@Test
	public void failYoungAccountBaseNot10() throws BankException, AccountException, ClientException {
		Client notSoYoungClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 18);

		try {
			this.bank.createAccount(AccountType.YOUNG, notSoYoungClient, 100, 100);
			fail();
		} catch (AccountException e) {
			assertEquals(0, this.bank.getTotalNumberOfAccounts());
		}
	}

	@Test
	public void successSalaryAccount() throws BankException, AccountException, ClientException {
		String iban = this.bank.createAccount(AccountType.SALARY, this.client, 100, 100);

		Account account = this.services.getAccountByIban(iban);

		assertTrue(account instanceof SalaryAccount);
		assertEquals(1, this.bank.getTotalNumberOfAccounts());
		assertEquals(this.client, account.getClient());
		assertEquals(100, account.getBalance());
		assertEquals(100, ((SalaryAccount) account).getSalary());
	}

	@Test(expected = BankException.class)
	public void clientHasAccountOfAnotherBank() throws AccountException, ClientException, BankException {
		Bank otherBank = new Bank("BPI");
		otherBank.createAccount(AccountType.CHECKING, this.client, 100, 0);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
