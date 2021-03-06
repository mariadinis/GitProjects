package sibs_domain;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import bank_exceptions.AccountException;
import bank_services.Services;
import sibs_exceptions.OperationException;
import sibs_exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	private Queue<TransferOperation> unprocessedTransactions;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.unprocessedTransactions = new LinkedBlockingQueue<TransferOperation>();
		this.services = services;
	}

	public int addOperation(String type, transferOperationData data) throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(this, data);
		} else {
			operation = new PaymentOperation(data.getTargetIban(), data.getAmount());
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int transfer(String sourceIban, String targetIban, int amount)
			throws OperationException, SibsException, AccountException {

		if (!services.checkAccountExistence(sourceIban) || !services.checkAccountExistence(targetIban)) {
			throw new SibsException();
		}

		/*
		 * Classe transferOperationData foi criada para respeitar guideline 4: "Keep
		 * Unit Interfaces Small" -> todos os metodos relacionados com o processo de
		 * transferencia usam estes 4 atributos.
		 */
		transferOperationData TOpData = new transferOperationData(this.services, sourceIban, targetIban, amount);

		int position = addOperation(Operation.OPERATION_TRANSFER, TOpData);
		unprocessedTransactions.add((TransferOperation) operations[position]);

		return position;
	}

	public void processOperations() throws SibsException, AccountException {
		while (!unprocessedTransactions.isEmpty()) {
			process();
		}
	}

	public void process() throws SibsException, AccountException {
		if (!unprocessedTransactions.isEmpty()) {
			unprocessedTransactions.peek().process(services);
		}
	}

	public void cancelOperation(int id) throws SibsException, AccountException {
		Operation op = operations[id];
		if (op instanceof TransferOperation) {
			((TransferOperation) op).cancel(services);
		}
	}

	public void finishProcessingTransaction() {
		unprocessedTransactions.poll();
	}

	public int getNumberOfUnprocessedOperations() {
		return unprocessedTransactions.size();
	}

}
