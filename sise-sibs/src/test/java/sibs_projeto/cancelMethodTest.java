package sibs_projeto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_exceptions.AccountException;
import bank_services.Services;
import sibs_domain.Cancelled;
import sibs_domain.Completed;
import sibs_domain.Error;
import sibs_domain.Sibs;
import sibs_domain.TransferOperation;
import sibs_exceptions.OperationException;
import sibs_exceptions.SibsException;

public class cancelMethodTest {

	private static final String sourceIban = "CGDCK1";
	private static final String targetIbanBANKEQUAL = "CGDCK2";
	private static final String targetIbanBANKDIFERENT = "BPICK2";
	private static final int amount = 100;
	private Services mockServices;
	private Sibs sibs;
	private TransferOperation op;

	@Before
	public void setUp() throws OperationException, SibsException, AccountException {
		mockServices = mock(Services.class);
		when(mockServices.checkAccountExistence(anyString())).thenReturn(true);
		sibs = new Sibs(100, mockServices);
	}

	@Test // Question 2
	public void RegistedToCancelled() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Cancelled);
		assertEquals(0, sibs.getNumberOfUnprocessedOperations());
	}

	@Test // Question 2
	public void WithdrawnToCancelled() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.process();
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Cancelled);
	}

	@Test // Question 2
	public void DepositedToCancelled() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.process();
		sibs.process();
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Cancelled);
	}

	@Test // Question 2
	public void FailFromCompletedToCancelled() throws OperationException, AccountException, SibsException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.process();
		sibs.process();
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Completed);
	}

	@Test // Question 2
	public void FailFromErrorToCancelled() throws OperationException, AccountException, SibsException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).when(mockServices).withdraw(anyString(), anyInt());
		sibs.process();
		sibs.process(); // Retry1
		sibs.process(); // Retry2
		sibs.process(); // Retry3
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Error);
	}

	@Test // Question 1
	public void failToProcessCancelled() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.cancelOperation(position);
		assertTrue(op.getState() instanceof Cancelled);

		sibs.process();
		assertTrue(op.getState() instanceof Cancelled);

	}

	@After
	public void tearDown() {
		sibs = null;
	}
}
