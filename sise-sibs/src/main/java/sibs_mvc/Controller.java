package sibs_mvc;

import java.util.ArrayList;

import bank_domain.Account;
import bank_domain.Client;
import bank_exceptions.AccountException;
import bank_services.Services;
import sibs_domain.Completed;
import sibs_domain.FinalState;
import sibs_domain.Sibs;
import sibs_domain.TransferOperation;
import sibs_domain.transferOperationData;
import sibs_exceptions.OperationException;
import sibs_exceptions.SibsException;

public class Controller {

	/*
	 * services passa a ser atributo do controller para nao ser invocado na
	 * interface, ou seja, a classe Interface passa a ser independente da classe
	 * Services -> guideline 5: "Separate Concerns in Modules"
	 */
	private Services services = new Services();

	public String associate(String iban, String phoneNumber) {
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

	public String transfer(transferOperationData phoneNumberData) {
		MBwayAccount sourceAccount = MBwayAccount.getMBwayAccountByPhoneNumber(phoneNumberData.getSourceIban());
		MBwayAccount targetAccount = MBwayAccount.getMBwayAccountByPhoneNumber(phoneNumberData.getTargetIban());

		if (!sourceAccount.isActive()) {
			return "This phone number " + phoneNumberData.getSourceIban() + " does not have a MBway account.";
		} else if (!targetAccount.isActive()) {
			return "This phone number " + phoneNumberData.getTargetIban() + " does not have a MBway account.";
		}

		String sourceIban = getIban(sourceAccount);
		String targetIban = getIban(targetAccount);

		Sibs sibs = new Sibs(10, this.services);
		transferOperationData data = new transferOperationData(this.services, sourceIban, targetIban,
				phoneNumberData.getAmount());
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
		while (!(op.getState() instanceof FinalState)) {
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

			transferOperationData data = new transferOperationData(this.services, friend, targetPhoneNumber,
					friendsAmount.get(i));
			String result = transfer(data);
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
