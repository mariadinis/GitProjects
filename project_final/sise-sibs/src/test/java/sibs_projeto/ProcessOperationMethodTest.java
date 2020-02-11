package sibs_projeto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_exceptions.AccountException;
import bank_services.Services;
import sibs_domain.Completed;
import sibs_domain.Error;
import sibs_domain.Sibs;
import sibs_domain.TransferOperation;
import sibs_exceptions.OperationException;
import sibs_exceptions.SibsException;

public class ProcessOperationMethodTest {

	private static final String sourceIban = "CGDCK1";
	private static final String targetIbanBANKEQUAL = "CGDCK2";
	private static final String targetIbanBANKDIFERENT = "BPICK2";
	private static final int amount = 100;
	private Services mockServices;
	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException, AccountException {
		mockServices = mock(Services.class);
		when(mockServices.checkAccountExistence(anyString())).thenReturn(true);
		sibs = new Sibs(100, mockServices);
	}

	@Test // Question 6
	public void allOperationCompleted() throws OperationException, SibsException, AccountException {
		TransferOperation op1 = (TransferOperation) sibs
				.getOperation(sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount));
		TransferOperation op2 = (TransferOperation) sibs
				.getOperation(sibs.transfer(targetIbanBANKDIFERENT, targetIbanBANKEQUAL, amount));
		TransferOperation op3 = (TransferOperation) sibs
				.getOperation(sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount));
		TransferOperation op4 = (TransferOperation) sibs
				.getOperation(sibs.transfer(targetIbanBANKEQUAL, targetIbanBANKDIFERENT, amount));
		assertEquals(4, sibs.getNumberOfUnprocessedOperations());

		sibs.processOperations();

		assertEquals(0, sibs.getNumberOfUnprocessedOperations());
		assertTrue(op1.getState() instanceof Completed);
		assertTrue(op2.getState() instanceof Completed);
		assertTrue(op3.getState() instanceof Completed);
		assertTrue(op4.getState() instanceof Completed);
	}

	@Test // Question 6
	public void allOperationProcessedToFinalStates() throws OperationException, SibsException, AccountException {
		TransferOperation op1 = (TransferOperation) sibs
				.getOperation(sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount));
		TransferOperation op2 = (TransferOperation) sibs
				.getOperation(sibs.transfer(targetIbanBANKDIFERENT, targetIbanBANKEQUAL, amount));
		TransferOperation op3 = (TransferOperation) sibs
				.getOperation(sibs.transfer(targetIbanBANKEQUAL, sourceIban, amount));
		TransferOperation op4 = (TransferOperation) sibs
				.getOperation(sibs.transfer(targetIbanBANKEQUAL, targetIbanBANKDIFERENT, amount));

		doThrow(new AccountException()).when(mockServices).withdraw(targetIbanBANKDIFERENT, amount);
		doThrow(new AccountException()).when(mockServices).withdraw(sourceIban, amount);
		assertEquals(4, sibs.getNumberOfUnprocessedOperations());

		sibs.processOperations();

		assertEquals(0, sibs.getNumberOfUnprocessedOperations());
		assertTrue(op1.getState() instanceof Error);
		assertTrue(op2.getState() instanceof Error);
		assertTrue(op3.getState() instanceof Completed);
		assertTrue(op4.getState() instanceof Completed);
	}

	@After
	public void tearDown() {
		sibs = null;
	}

}
