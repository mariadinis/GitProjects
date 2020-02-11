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

public class GetTotalValueOfOperationsForTypeMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";

	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
		transferOperationData data1 = new transferOperationData(new Services(), null, TARGET_IBAN, 100);
		transferOperationData data2 = new transferOperationData(new Services(), SOURCE_IBAN, TARGET_IBAN, 200);
		this.sibs.addOperation(Operation.OPERATION_PAYMENT, data1);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, data2);
	}

	@Test
	public void successTwo() throws SibsException, OperationException {
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
