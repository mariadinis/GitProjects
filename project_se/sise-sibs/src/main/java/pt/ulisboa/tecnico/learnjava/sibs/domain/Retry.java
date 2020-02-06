package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Retry extends State {

	private static int count;
	private String previousState;

	public Retry(TransferOperation operation, String previousState) {
		super(operation);
		this.previousState = previousState;
	}

	@Override
	public void process(Services services, String sourceIban, String targetIban, int amount) {
		++count;
		TransferOperation op = getOperation();
		SetState(op);
		op.getState().process(services, sourceIban, targetIban, amount);
		if (!(op.getState() instanceof Retry)) {
			count = 0;
		} else if (count == 3) {
			count = 0;
			SetState(op);
			op.getState().undo(services, sourceIban, targetIban, amount);

			op.setState(new Error(op));
		}

	}

	private void SetState(TransferOperation op) {
		switch (previousState) {
		case "REGISTERED":
			op.setState(new Registered(op));
			break;
		case "WITHDRAWN":
			op.setState(new Withdrawn(op));
			break;
		case "DEPOSITED":
			op.setState(new Deposited(op));
			break;
		}
	}

	@Override
	public void undo(Services services, String sourceIban, String targetIban, int amount) {
	}

	@Override
	public void cancel(Services services, String sourceIban, String targetIban, int amount) {
	}

}
