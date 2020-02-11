package bank_account;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_domain.Bank;
import bank_domain.CheckingAccount;
import bank_domain.Client;
import bank_domain.SalaryAccount;
import bank_domain.SavingsAccount;
import bank_domain.Bank.AccountType;
import bank_exceptions.AccountException;
import bank_exceptions.BankException;
import bank_exceptions.ClientException;

public class GetAccountIdMethodTest {
	private static final String OWNER_NAME = "Simão";

	private CheckingAccount checking;
	private SavingsAccount savings;
	private SalaryAccount salary;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		Bank bank = new Bank("CGD");

		Client client = new Client(bank, "José", "Manuel", "123456789", "987654321", "Street", 33);

		this.checking = new CheckingAccount(client, 100);
		this.savings = new SavingsAccount(client, 100, 10);
		this.salary = new SalaryAccount(client, 100, 1000);
	}

	@Test
	public void successForCheckingAccount() {
		assertTrue(this.checking.getAccountId().startsWith(AccountType.CHECKING.getPrefix()));
	}

	@Test
	public void successForSavingsAccount() {
		assertTrue(this.savings.getAccountId().startsWith(AccountType.SAVINGS.getPrefix()));
	}

	@Test
	public void successForSalaryAccount() {
		assertTrue(this.salary.getAccountId().startsWith(AccountType.SALARY.getPrefix()));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
