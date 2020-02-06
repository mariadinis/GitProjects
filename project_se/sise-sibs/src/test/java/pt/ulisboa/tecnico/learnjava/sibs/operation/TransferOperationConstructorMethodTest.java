package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperationConstructorMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;
	private Sibs sibs;

	@Before
	public void setUp() {
		Services services = new Services();
		sibs = new Sibs(100, services);
	}

	@Test
	public void success() throws OperationException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		TransferOperation operation = new TransferOperation(sibs, SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

	@Test(expected = OperationException.class)
	public void nonPositiveValue() throws OperationException {
		new TransferOperation(sibs, SOURCE_IBAN, TARGET_IBAN, 0);
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new TransferOperation(sibs, null, TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new TransferOperation(sibs, "", TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new TransferOperation(sibs, SOURCE_IBAN, null, 100);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new TransferOperation(sibs, SOURCE_IBAN, "", 100);
	}

	@After
	public void tearDown() {
		sibs = null;
	}

}
