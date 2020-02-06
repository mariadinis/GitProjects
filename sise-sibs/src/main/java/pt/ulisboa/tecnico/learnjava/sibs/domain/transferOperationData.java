package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class transferOperationData {

	private String sourceIban;
	private String targetIban;
	private int amount;
	private Services services;

	public transferOperationData(Services services, String sourceIban, String targetIban, int amount) {
		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.amount = amount;
		this.services = services;
	}

	public String getSourceIban() {
		return sourceIban;
	}

	public String getTargetIban() {
		return targetIban;
	}

	public int getAmount() {
		return amount;
	}

	public Services getServices() {
		return services;
	}

}
