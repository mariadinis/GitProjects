package pt.ulisboa.tecnico.learnjava.sibs.mvc;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class Interface {

	public static void main(String[] args) throws BankException, ClientException, AccountException {
		Bank bank = new Bank("CGD");

		Client client1 = new Client(bank, "Mario", "Jorge", "123456789", "456123789", "Rua", 60);
		bank.addClient(client1);
		bank.createAccount(AccountType.CHECKING, client1, 100, 0);

		Client client2 = new Client(bank, "Jorge", "Junior", "123789456", "789456123", "Rua", 60);
		bank.addClient(client2);
		bank.createAccount(AccountType.CHECKING, client2, 100, 0);

		Controller c = new Controller();

		Scanner s;
		while (true) {
			s = new Scanner(new BufferedInputStream(System.in));

			switch (s.next()) {
			default:
				System.out.println("Invalid input. Try again.");
				break;
			case "exit":
				System.exit(0);
				break;
			case "associate-mbway":
				// associate-mbway CGDCK1 987654321
				// associate-mbway CGDCK2 987321654
				System.out.println(c.associate(s.next(), s.next()));
				break;
			case "confirm-mbway":
				// confirm-mbway 987654321 (code)
				// confirm-mbway 987321654 (code)
				System.out.println(c.confirm(s.next(), s.nextInt()));
				break;
			case "mbway-transfer":
				// mbway-transfer 987654321 987321654 50
				System.out.println(c.transfer(s.next(), s.next(), s.nextInt()));
				break;
			case "mbway-split-bill":
				// mbway-split-bill 2 20
				// friend 987321654 10
				// friend 987654321 10
				// end

				int numberOfFriends = s.nextInt();
				int amount = s.nextInt();
				ArrayList<String> friendsPhoneNumber = new ArrayList<String>();
				ArrayList<Integer> friendsAmount = new ArrayList<Integer>();
				boolean proceed = true;
				while (proceed) {
					Scanner ss = new Scanner(new BufferedInputStream(System.in));
					switch (ss.next()) {
					case "end":
						proceed = false;
						break;
					case "friend":
						friendsPhoneNumber.add(ss.next());
						friendsAmount.add(ss.nextInt());
						break;
					default:
						System.out.println("Invalid input. Please insert a friend.");
						break;
					}
					ss.close();
				}
				System.out.println(c.split_bill(numberOfFriends, amount, friendsPhoneNumber, friendsAmount));

			}
			s.close();
		}
	}

}
