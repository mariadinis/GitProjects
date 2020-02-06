package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Registered extends State {

	public Registered(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(Services services, String sourceIban, String targetIban, int amount) {
		TransferOperation op = getOperation();
		try {
			services.withdraw(sourceIban, amount);
			op.setState(new Withdrawn(op));
		} catch (AccountException e) {
			op.setState(new Retry(op, "REGISTERED"));
		}

	}

	@Override
	public void undo(Services services, String sourceIban, String targetIban, int amount) {

	}

	@Override
	public void cancel(Services services, String sourceIban, String targetIban, int amount) {
		TransferOperation op = getOperation();
		op.setState(new Cancelled(op));

	}

}
