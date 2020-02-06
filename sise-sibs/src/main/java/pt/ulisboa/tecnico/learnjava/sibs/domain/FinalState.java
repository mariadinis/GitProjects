package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class FinalState extends State {

	/*
	 * States Completed, Cancelled e Error sao agora especializacoes da classe
	 * FinalState -> guideline 3: "Write Code Once"
	 */
	public FinalState(TransferOperation operation) {
		super(operation);
		operation.getSibs().finishProcessingTransaction();
	}

	@Override
	public void process(transferOperationData data) {

	}

	@Override
	public void undo(transferOperationData data) {

	}

	@Override
	public void cancel(transferOperationData data) {

	}

}
