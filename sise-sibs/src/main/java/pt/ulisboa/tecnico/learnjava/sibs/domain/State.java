package pt.ulisboa.tecnico.learnjava.sibs.domain;

public abstract class State {

	private TransferOperation operation;

	public State(TransferOperation operation) {
		this.operation = operation;
	}

	public TransferOperation getOperation() {
		return operation;
	}

	public abstract void process(transferOperationData data);

	public abstract void undo(transferOperationData data);

	public abstract void cancel(transferOperationData data);

}
