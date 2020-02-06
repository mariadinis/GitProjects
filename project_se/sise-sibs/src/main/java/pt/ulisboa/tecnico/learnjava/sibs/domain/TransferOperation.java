package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private Sibs sibs;
	private State state;
	private int amount;

	public TransferOperation(Sibs sibs, String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.amount = value;
		this.sibs = sibs;
		this.state = new Registered(this);
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State newState) {
		this.state = newState;
	}

	public void process(Services services) throws AccountException {
		state.process(services, sourceIban, targetIban, amount);
	}

	public void cancel(Services services) throws AccountException {
		state.cancel(services, sourceIban, targetIban, amount);

	}

	public Sibs getSibs() {
		return this.sibs;
	}

}
