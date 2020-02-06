package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private transferOperationData data;
	private Sibs sibs;
	private State state;

	public TransferOperation(Sibs sibs, transferOperationData data) throws OperationException {
		super(Operation.OPERATION_TRANSFER, data.getAmount());

		if (invalidString(data.getSourceIban()) || invalidString(data.getTargetIban())) {
			throw new OperationException();
		}

		this.data = data;
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
		return this.data.getSourceIban();
	}

	public String getTargetIban() {
		return this.data.getTargetIban();
	}

	public State getState() {
		return this.state;
	}

	public void setState(State newState) {
		this.state = newState;
	}

	public void process(Services services) throws AccountException {
		state.process(data);
	}

	public void cancel(Services services) throws AccountException {
		state.cancel(data);

	}

	public Sibs getSibs() {
		return this.sibs;
	}

}
