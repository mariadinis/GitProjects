package sibs_operation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sibs_domain.Operation;
import sibs_domain.PaymentOperation;
import sibs_exceptions.OperationException;

public class PaymentOperationConstructorMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	@Test
	public void success() throws OperationException {
		PaymentOperation operation = new PaymentOperation(TARGET_IBAN, VALUE);

		assertEquals(Operation.OPERATION_PAYMENT, operation.getType());
		assertEquals(VALUE, operation.getValue());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new PaymentOperation(null, VALUE);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new PaymentOperation("", VALUE);
	}

}
