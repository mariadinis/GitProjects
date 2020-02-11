package sibs_domain;

import bank_exceptions.AccountException;

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
		int amount = data.getAmount();
		TransferOperation op = getOperation();

		try {
			data.getServices().deposit(data.getSourceIban(), amount);
			data.getServices().withdraw(data.getTargetIban(), amount);
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
