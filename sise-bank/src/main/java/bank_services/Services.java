package bank_services;

import bank_domain.Account;
import bank_domain.Bank;
import bank_exceptions.AccountException;

public class Services {
	public void deposit(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.deposit(amount);
	}

	public void withdraw(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.withdraw(amount);
	}

	public Account getAccountByIban(String iban) {
		String code = iban.substring(0, 3);
		String accountId = iban.substring(3);

		Bank bank = Bank.getBankByCode(code);
		Account account = bank.getAccountByAccountId(accountId);

		return account;
	}

	public boolean checkAccountExistence(String iban) {
		String bankCode = iban.substring(0, 3);
		Account account = Bank.getBankByCode(bankCode).getAccountByAccountId(iban.substring(3));
		return account != null;
	}

}
