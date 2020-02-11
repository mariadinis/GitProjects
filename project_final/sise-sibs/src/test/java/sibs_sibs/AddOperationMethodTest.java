package sibs_sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank_services.Services;
import sibs_domain.Operation;
import sibs_domain.Sibs;
import sibs_domain.transferOperationData;
import sibs_exceptions.OperationException;
import sibs_exceptions.SibsException;

public class AddOperationMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Sibs sibs;

	@Before
	public void setUp() {
		this.sibs = new Sibs(3, new Services());
	}

	@Test
	public void success() throws OperationException, SibsException {
		transferOperationData data = new transferOperationData(new Services(), SOURCE_IBAN, TARGET_IBAN, VALUE);
		int position = this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);

		Operation operation = this.sibs.getOperation(position);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(VALUE, operation.getValue());
	}

	@Test
	public void successWithDelete() throws OperationException, SibsException {
		transferOperationData data = new transferOperationData(new Services(), SOURCE_IBAN, TARGET_IBAN, VALUE);
		int position = this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.removeOperation(position);
		transferOperationData data2 = new transferOperationData(new Services(), null, TARGET_IBAN, 200);
		position = this.sibs.addOperation(Operation.OPERATION_PAYMENT, data2);

		Operation operation = this.sibs.getOperation(position);

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_PAYMENT, operation.getType());
		assertEquals(200, operation.getValue());
	}

	@Test(expected = SibsException.class)
	public void failIsFull() throws OperationException, SibsException {
		transferOperationData data = new transferOperationData(new Services(), SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
