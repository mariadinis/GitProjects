package bank_account;

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
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;

public class ConstructorMethodTest {
	private static final int AMOUNT = 100;

	private Bank bank;
	private Client client;

	@Before
	public void setUp() throws BankException, ClientException {
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void success() throws AccountException, ClientException {
		Account account = new CheckingAccount(this.client, AMOUNT);

		assertEquals(this.client, account.getClient());
		assertEquals(AMOUNT, account.getBalance());
		assertTrue(this.client.hasAccount(account));
	}

	@Test
	public void nullClient() throws ClientException {
		try {
			new CheckingAccount(null, AMOUNT);
			fail();
		} catch (AccountException e) {
			// passes

			/*
			 * bloco catch sem codigo e considerado codigo morto, mas neste caso nao ha nada
			 * a fazer
			 */
		}
	}

	@Test
	public void limitOfAccountsPerClient() throws AccountException, ClientException {
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);

		try {
			new CheckingAccount(this.client, AMOUNT);
			fail();
		} catch (ClientException e) {
			assertEquals(5, this.client.getNumberOfAccounts());
		}

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
