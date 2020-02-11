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

public class GetNumberOfOperationsMethodTest {
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
	public void success() throws SibsException, OperationException {
		this.sibs.addOperation(Operation.OPERATION_PAYMENT, data);

		assertEquals(2, this.sibs.getNumberOfOperations());
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
