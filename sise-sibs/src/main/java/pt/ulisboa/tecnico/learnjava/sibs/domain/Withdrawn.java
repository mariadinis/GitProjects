package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Withdrawn extends State {

	public Withdrawn(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(transferOperationData data) {
		TransferOperation op = getOperation();
		String targetIban = data.getTargetIban();

		try {
			data.getServices().deposit(targetIban, data.getAmount());
			if (data.getSourceIban().substring(0, 3).equals(targetIban.substring(0, 3))) {
				op.setState(new Completed(op));
			} else {
				op.setState(new Deposited(op));
			}
		} catch (Exception e) {
			op.setState(new Retry(op, "WITHDRAWN"));
		}

	}

	@Override
	public void undo(transferOperationData data) {
		TransferOperation op = getOperation();

		try {
			data.getServices().deposit(data.getSourceIban(), data.getAmount());
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
