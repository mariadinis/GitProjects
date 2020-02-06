package pt.ulisboa.tecnico.learnjava.sibs.mvc;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;

public class MBwayAccount {

	private Client client;
	private Account account;
	private int code;
	private boolean active;
	private static HashMap<String, MBwayAccount> MBwayAccountsList = new HashMap<String, MBwayAccount>();

	public MBwayAccount(Client client, Account account, int code) {
		this.client = client;
		this.account = account;
		this.code = code;
		this.active = false;
		MBwayAccountsList.put(client.getPhoneNumber(), this);
	}

	public void activate() {
		this.active = true;
	}

	public boolean isActive() {
		return this.active;
	}

	public Client getClient() {
		return client;
	}

	public Account getAccount() {
		return account;
	}

	public int getCode() {
		return code;
	}

	public static MBwayAccount getMBwayAccountByPhoneNumber(String phoneNUmber) {
		return MBwayAccountsList.get(phoneNUmber);
	}

}
