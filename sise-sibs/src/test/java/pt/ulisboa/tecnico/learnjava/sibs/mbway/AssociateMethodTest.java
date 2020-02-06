package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class AssociateMethodTest {

	/**/

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		Bank bank = new Bank("CGD");

		Client client1 = new Client(bank, "Mario", "Jorge", "123456789", "456123789", "Rua", 60);
		bank.addClient(client1);
		bank.createAccount(AccountType.CHECKING, client1, 100, 0);

		Client client2 = new Client(bank, "Jorge", "Junior", "123789456", "789456123", "Rua", 60);
		bank.addClient(client2);
		bank.createAccount(AccountType.CHECKING, client2, 100, 0);

	}

	@Test
	public void test() {

	}

	@After
	public void tearDown() {

	}
}
