package pt.ulisboa.tecnico.learnjava.sibs.projeto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ProcessMethodTest {

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

	@Test // Question 1
	public void registerTransferOperation() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		assertTrue(op.getState() instanceof Registered);
		assertEquals(1, sibs.getNumberOfUnprocessedOperations());
	}

	@Test // Question 1
	public void correctSequenceWithEqualBank() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.process();
		assertTrue(op.getState() instanceof Withdrawn);
		sibs.process();
		assertTrue(op.getState() instanceof Completed);

		assertEquals(0, sibs.getNumberOfUnprocessedOperations());
	}

	@Test // Question 1
	public void correctSequenceWithDifferentBank() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount);
		op = (TransferOperation) sibs.getOperation(position);
		assertEquals(1, sibs.getNumberOfUnprocessedOperations());

		sibs.process();
		assertTrue(op.getState() instanceof Withdrawn);
		sibs.process();
		assertTrue(op.getState() instanceof Deposited);
		sibs.process();
		assertTrue(op.getState() instanceof Completed);
		assertEquals(0, sibs.getNumberOfUnprocessedOperations());
	}

	@Test // Question 1 / 8
	public void RegisteredToError() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).when(mockServices).withdraw(anyString(), anyInt());

		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry2
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry3
		assertTrue(op.getState() instanceof Error);
	}

	@Test // Question 1 / 8
	public void WithdrawnToError() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).when(mockServices).deposit(targetIbanBANKEQUAL, amount);

		sibs.process();
		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry2
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry3
		verify(mockServices).withdraw(sourceIban, amount);
		verify(mockServices).deposit(sourceIban, amount);
		assertTrue(op.getState() instanceof Error);
	}

	@Test // Question 1
	public void DepositedToError() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).when(mockServices).withdraw(sourceIban, op.commission());

		sibs.process();
		sibs.process();
		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry2
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry3
		verify(mockServices).withdraw(sourceIban, amount);
		verify(mockServices).deposit(targetIbanBANKDIFERENT, amount);
		verify(mockServices).deposit(sourceIban, amount);
		verify(mockServices).withdraw(targetIbanBANKDIFERENT, amount);
		assertTrue(op.getState() instanceof Error);
	}

	@Test // Question 1
	public void failToProcessError() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).when(mockServices).withdraw(anyString(), anyInt());
		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry2
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry3
		assertTrue(op.getState() instanceof Error);

		sibs.process();
		assertTrue(op.getState() instanceof Error);
	}

	@Test // Question 1
	public void failToProcessCompleted() throws OperationException, SibsException, AccountException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		sibs.process();
		sibs.process();
		assertTrue(op.getState() instanceof Completed);

		sibs.process();
		assertTrue(op.getState() instanceof Completed);

	}

	@Test // Question 8
	public void successFirstRetry() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).doNothing().when(mockServices).withdraw(anyString(), anyInt());

		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Withdrawn);
	}

	@Test // Question 8
	public void successSecondRetry() throws SibsException, AccountException, OperationException {
		int position = sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
		op = (TransferOperation) sibs.getOperation(position);
		doThrow(new AccountException()).doThrow(new AccountException()).doNothing().when(mockServices)
				.deposit(targetIbanBANKEQUAL, amount);

		sibs.process();
		sibs.process();
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry1
		assertTrue(op.getState() instanceof Retry);
		sibs.process(); // Retry2
		assertTrue(op.getState() instanceof Completed);
	}

	@After
	public void tearDown() {
		sibs = null;
		mockServices = null;
	}
}
