package pt.ulisboa.tecnico.learnjava.sibs.mvc;

import java.util.ArrayList;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.transferOperationData;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Controller {

	public String associate(String iban, String phoneNumber) {
		Services services = new Services();
		Account account = services.getAccountByIban(iban);
		Client client = account.getClient();

		client.setPhoneNumber(phoneNumber);

		int code = (int) (Math.random() * ((999999 - 100000) + 1) + 100000);
		new MBwayAccount(client, account, code);

		return "Code: " + code + " (don't share it with anyone)";
	}

	public String confirm(String phoneNumber, int code) {
		MBwayAccount mbwayAccount = MBwayAccount.getMBwayAccountByPhoneNumber(phoneNumber);
		if (mbwayAccount.getCode() == code) {
			mbwayAccount.activate();
			return "MBWay Association Confirmed Successfully!";
		} else {
			return "Wrong confirmation code!";
		}
	}

	public String transfer(String sourcePhoneNumber, String targetPhoneNumber, int amount) {
		MBwayAccount sourceAccount = MBwayAccount.getMBwayAccountByPhoneNumber(sourcePhoneNumber);
		MBwayAccount targetAccount = MBwayAccount.getMBwayAccountByPhoneNumber(targetPhoneNumber);

		if (!sourceAccount.isActive()) {
			return "This phone number " + sourcePhoneNumber + " does not have a MBway account.";
		} else if (!targetAccount.isActive()) {
			return "This phone number " + targetPhoneNumber + " does not have a MBway account.";
		}

		String sourceIban = getIban(sourceAccount);
		String targetIban = getIban(targetAccount);

		Services services = new Services();
		Sibs sibs = new Sibs(10, services);
		transferOperationData data = new transferOperationData(services, sourceIban, targetIban, amount);
		try {
			/*
			 * metodo processTransfer criado para respeitar as guidelines 1:
			 * "Write Short Units of Code" e 2: "Write Simple Units of Code"
			 */
			return processTransfer(sibs, data);
		} catch (OperationException | SibsException | AccountException e) {
			return "Could not complete transfer!";
		}
	}

	private String processTransfer(Sibs sibs, transferOperationData data)
			throws SibsException, OperationException, AccountException {
		TransferOperation op = (TransferOperation) sibs
				.getOperation(sibs.transfer(data.getSourceIban(), data.getTargetIban(), data.getAmount()));
		while (!(op.getState() instanceof Completed || op.getState() instanceof Error
				|| op.getState() instanceof Cancelled)) {
			sibs.process();
		}
		if (op.getState() instanceof Completed) {
			return "Transfer successful!";
		} else {
			return "Could not complete transfer!";
		}
	}

	private String getIban(MBwayAccount account) {
		String accountId = account.getAccount().getAccountId();
		String bankCode = account.getClient().getBank().getCode();
		return bankCode + accountId;
	}

	public String split_bill(int numberOfFriends, int totalamount, ArrayList<String> friendsPhoneNumber,
			ArrayList<Integer> friendsAmount) {
		if (friendsPhoneNumber.size() > numberOfFriends) {
			return "Oh no! Too many friends.";
		} else if (friendsPhoneNumber.size() < numberOfFriends) {
			return "Oh no! One friend is missing.";
		} else if (sum(friendsAmount) != totalamount) {
			return "Something is wrong. Did you set the bill amount right?";
		}
		String targetPhoneNumber = friendsPhoneNumber.get(0);

		/*
		 * metodo receiveMoneyFromFriends criado para respeitar as guidelines 1:
		 * "Write Short Units of Code" e 2: "Write Simple Units of Code"
		 */
		return receiveMoneyFromFriends(numberOfFriends, friendsPhoneNumber, friendsAmount, targetPhoneNumber);
	}

	private String receiveMoneyFromFriends(int numberOfFriends, ArrayList<String> friendsPhoneNumber,
			ArrayList<Integer> friendsAmount, String targetPhoneNumber) {
		for (int i = 1; i < numberOfFriends; i++) {
			String friend = friendsPhoneNumber.get(i);
			if (!MBwayAccount.getMBwayAccountByPhoneNumber(friend).isActive()) {
				return "This phone number " + friend + " does not have a MBway account.";
			}
			String result = transfer(friend, targetPhoneNumber, friendsAmount.get(i));
			if (!result.equals("Transfer successful!")) {
				return "Oh no! One of your friends does not have money to pay!";
			}
		}
		return "Bill payed successfully!";
	}

	private int sum(ArrayList<Integer> friendsAmount) {
		return friendsAmount.stream().reduce(0, (a, b) -> a + b);
	}
}
