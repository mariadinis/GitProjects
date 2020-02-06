package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class Retry extends State {

	private static int count;
	private String previousState;

	public Retry(TransferOperation operation, String previousState) {
		super(operation);
		this.previousState = previousState;
	}

	@Override
	public void process(transferOperationData data) {
		++count;
		TransferOperation op = getOperation();
		SetState(op);
		op.getState().process(data);
		if (!(op.getState() instanceof Retry)) {
			count = 0;
		} else if (count == 3) {
			count = 0;
			SetState(op);
			op.getState().undo(data);

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
	public void undo(transferOperationData data) {
	}

	@Override
	public void cancel(transferOperationData data) {
	}

}
