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

public class DeleteClientMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Bank bank;
	private Client client;

	@Before
	public void setUp() throws BankException, ClientException {
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
	}

	@Test
	public void success() throws BankException, AccountException {
		this.bank.deleteClient(NIF);

		assertEquals(0, this.bank.getTotalNumberOfClients());
	}

	@Test
	public void successClientWithAccounts() throws BankException, AccountException, ClientException {
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);

		this.bank.deleteClient(NIF);

		assertEquals(0, this.bank.getTotalNumberOfClients());
		assertEquals(0, this.bank.getTotalNumberOfAccounts());
	}

	@Test(expected = BankException.class)
	public void noClientForNif() throws BankException, AccountException {
		this.bank.deleteClient("123456780");
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
