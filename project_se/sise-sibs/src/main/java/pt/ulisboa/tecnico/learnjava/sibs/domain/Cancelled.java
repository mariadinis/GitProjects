package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Cancelled extends State {

	public Cancelled(TransferOperation operation) {
		super(operation);
		operation.getSibs().finishProcessingTransaction();
	}

	@Override
	public void process(Services services, String sourceIban, String targetIban, int amount) {

	}

	@Override
	public void undo(Services services, String sourceIban, String targetIban, int amount) {

	}

	@Override
	public void cancel(Services services, String sourceIban, String targetIban, int amount) {

	}

}
