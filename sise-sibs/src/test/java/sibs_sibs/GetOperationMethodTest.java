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

public class GetOperationMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Sibs sibs;
	private transferOperationData data;

	@Before
	public void setUp() throws OperationException, SibsException {
		data = new transferOperationData(new Services(), null, TARGET_IBAN, VALUE);
		this.sibs = new Sibs(3, new Services());
		this.sibs.addOperation(Operation.OPERATION_PAYMENT, data);
	}

	@Test
	public void success() throws SibsException {
		Operation operation = this.sibs.getOperation(0);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_PAYMENT, operation.getType());
		assertEquals(VALUE, operation.getValue());
	}

	@Test(expected = SibsException.class)
	public void negativePosition() throws SibsException {
		this.sibs.getOperation(-1);
	}

	@Test(expected = SibsException.class)
	public void positionAboveLength() throws SibsException {
		this.sibs.getOperation(4);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
