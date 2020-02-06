package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Deposited extends State {

	public Deposited(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(Services services, String sourceIban, String targetIban, int amount) {
		TransferOperation op = getOperation();
		try {
			services.withdraw(sourceIban, op.commission());
			op.setState(new Completed(op));
		} catch (AccountException e) {
			op.setState(new Retry(op, "DEPOSITED"));
		}

	}

	@Override
	public void undo(Services services, String sourceIban, String targetIban, int amount) {
		try {
			services.deposit(sourceIban, amount);
			services.withdraw(targetIban, amount);
			TransferOperation op = getOperation();
			op.setState(new Registered(op));
		} catch (AccountException e) {
		}

	}

	@Override
	public void cancel(Services services, String sourceIban, String targetIban, int amount) {
		TransferOperation op = getOperation();
		undo(services, sourceIban, targetIban, amount);
		op.setState(new Cancelled(op));

	}

}
