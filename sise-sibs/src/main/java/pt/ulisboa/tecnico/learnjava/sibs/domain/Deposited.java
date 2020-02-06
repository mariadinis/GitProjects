package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Deposited extends State {

	public Deposited(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(transferOperationData data) {
		TransferOperation op = getOperation();
		try {
			data.getServices().withdraw(data.getSourceIban(), op.commission());
			op.setState(new Completed(op));
		} catch (AccountException e) {
			op.setState(new Retry(op, "DEPOSITED"));
		}

	}

	@Override
	public void undo(transferOperationData data) {
		Services services = data.getServices();
		int amount = data.getAmount();
		TransferOperation op = getOperation();

		try {
			services.deposit(data.getSourceIban(), amount);
			services.withdraw(data.getTargetIban(), amount);
			op.setState(new Registered(op));
		} catch (AccountException e) {
			op.setState(new Error(op));
		}

	}

	@Override
	public void cancel(transferOperationData data) {
		TransferOperation op = getOperation();
		undo(data);
		op.setState(new Cancelled(op));

	}

}
