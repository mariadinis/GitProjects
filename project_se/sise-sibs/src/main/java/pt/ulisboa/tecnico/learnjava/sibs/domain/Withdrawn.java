package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Withdrawn extends State {

	public Withdrawn(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(Services services, String sourceIban, String targetIban, int amount) {
		TransferOperation op = getOperation();
		try {
			services.deposit(targetIban, amount);
			if (sourceIban.substring(0, 3).equals(targetIban.substring(0, 3))) {
				op.setState(new Completed(op));
			} else {
				op.setState(new Deposited(op));
			}
		} catch (Exception e) {
			op.setState(new Retry(op, "WITHDRAWN"));
		}

	}

	@Override
	public void undo(Services services, String sourceIban, String targetIban, int amount) {
		try {
			TransferOperation op = getOperation();
			services.deposit(sourceIban, amount);
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
