package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public abstract class State {

	private TransferOperation operation;

	public State(TransferOperation operation) {
		this.operation = operation;
	}

	public TransferOperation getOperation() {
		return operation;
	}

	public abstract void process(Services services, String sourceIban, String targetIban, int amount);

	public abstract void undo(Services services, String sourceIban, String targetIban, int amount);

	public abstract void cancel(Services services, String sourceIban, String targetIban, int amount);

}
