package bank_bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;

import bank_domain.Bank;
import bank_exceptions.BankException;

public class GetBankByCodeMethodTest {

	private static final String CODE = "CGD";

	@Test
	public void success() throws BankException {
		new Bank(CODE);

		assertEquals(CODE, Bank.getBankByCode(CODE).getCode());
	}

	@Test
	public void doesNotExist() throws BankException {
		new Bank(CODE);

		assertNull(Bank.getBankByCode("XYZ"));
	}

	@Test
	public void codeIsNull() throws BankException {
		new Bank(CODE);

		assertNull(Bank.getBankByCode(null));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
