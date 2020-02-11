package bank_bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Account;
import bank_domain.Bank;
import bank_domain.Client;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;
import bank_services.Services;

public class DeleteAccountMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Bank bank;
	private Client client;
	private Account account;
	private Services services;

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.services = new Services();
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		String iban = this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.account = this.services.getAccountByIban(iban);
	}

	@Test
	public void success() throws BankException, AccountException {
		this.bank.deleteAccount(this.account);

		assertEquals(0, this.bank.getTotalNumberOfAccounts());
		assertFalse(this.client.hasAccount(this.account));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
