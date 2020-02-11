package sibs_domain;

import bank_exceptions.AccountException;

public class Registered extends State {

	public Registered(TransferOperation operation) {
		super(operation);
	}

	@Override
	public void process(transferOperationData data) {
		TransferOperation op = getOperation();
		try {
			data.getServices().withdraw(data.getSourceIban(), data.getAmount());
			op.setState(new Withdrawn(op));
		} catch (AccountException e) {
			op.setState(new Retry(op, "REGISTERED"));
		}

	}

	@Override
	public void undo(transferOperationData data) {

	}

	@Override
	public void cancel(transferOperationData data) {
		TransferOperation op = getOperation();
		op.setState(new Cancelled(op));

	}

}
