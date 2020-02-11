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

public class RemoveOperationMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
		transferOperationData data = new transferOperationData(new Services(), null, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_PAYMENT, data);
	}

	@Test
	public void success() throws SibsException {
		this.sibs.removeOperation(0);

		assertEquals(0, this.sibs.getNumberOfOperations());
	}

	@Test(expected = SibsException.class)
	public void negativePosition() throws SibsException {
		this.sibs.removeOperation(-1);
	}

	@Test(expected = SibsException.class)
	public void positionAboveLength() throws SibsException {
		this.sibs.removeOperation(4);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
