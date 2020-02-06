package pt.ulisboa.tecnico.learnjava.sibs.projeto;

public class NewTransferMethodTest {
//	private static final String sourceIban = "CGDCK1";
//	private static final String targetIbanBANKEQUAL = "CGDCK2";
//	private static final String targetIbanBANKDIFERENT = "BPICK2";
//	private static final int amount = 100;
//	private Services mockServices;
//	private Sibs sibs;
//
//	@Before
//	public void setUp() {
//		mockServices = mock(Services.class);
//		when(mockServices.checkAccountExistence(anyString())).thenReturn(true);
//
//		sibs = new Sibs(100, mockServices);
//	}
//
//	@Test // Question 4
//	public void success_withMockedServices() throws SibsException, AccountException, OperationException {
//
//		sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//
//		verify(mockServices).withdraw(sourceIban, amount);
//		verify(mockServices).deposit(targetIbanBANKEQUAL, amount);
//		assertEquals(1, sibs.getNumberOfOperations());
//		assertEquals(100, sibs.getTotalValueOfOperations());
//		assertEquals(100, sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
//		assertEquals(0, sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
//	}
//
//	@Test // Question 5 (1)
//	public void accountExistsInBank() throws SibsException, AccountException, OperationException {
//
//		sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//
//		verify(mockServices).checkAccountExistence(sourceIban);
//		verify(mockServices).checkAccountExistence(targetIbanBANKEQUAL);
//		verify(mockServices).withdraw(sourceIban, amount);
//		verify(mockServices).deposit(targetIbanBANKEQUAL, amount);
//	}
//
//	@Test // Question 5 (1)
//	public void sourceAccountDoesNotExistsInBank() throws AccountException, OperationException {
//
//		when(mockServices.checkAccountExistence(anyString())).thenReturn(false);
//		try {
//			sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//			fail();
//		} catch (SibsException e) {
//			verify(mockServices).checkAccountExistence(sourceIban);
//			verify(mockServices, never()).checkAccountExistence(targetIbanBANKEQUAL);
//			verify(mockServices, never()).withdraw(sourceIban, amount);
//			verify(mockServices, never()).deposit(targetIbanBANKEQUAL, amount);
//		}
//
//	}
//
//	@Test // Question 5 (1)
//	public void targetAccountDoesNotExistsInBank() throws SibsException, AccountException, OperationException {
//
//		when(mockServices.checkAccountExistence(anyString())).thenReturn(true, false);
//		try {
//			sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//			fail();
//		} catch (SibsException e) {
//			verify(mockServices).checkAccountExistence(sourceIban);
//			verify(mockServices).checkAccountExistence(targetIbanBANKEQUAL);
//			verify(mockServices, never()).withdraw(sourceIban, amount);
//			verify(mockServices, never()).deposit(targetIbanBANKEQUAL, amount);
//		}
//	}
//
//	@Test // Question 5 (2)
//	public void checkTransactionFeeSameBank() throws SibsException, AccountException, OperationException {
//
//		sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//
//		assertEquals(0, 6);
//		verify(mockServices).withdraw(sourceIban, amount);
//		verify(mockServices).deposit(targetIbanBANKEQUAL, amount);
//
//	}
//
//	@Test // Question 5 (2)(3)
//	public void checkTransactionFeeDiferentBank() throws SibsException, AccountException, OperationException {
//
//		sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount);
//
//		verify(mockServices).withdraw(sourceIban, amount + 6);
//		verify(mockServices).deposit(targetIbanBANKDIFERENT, amount);
//
//	}
//
//	@Test // Question 6
//	public void testFailWithdraw() throws SibsException, OperationException, AccountException {
//
//		doThrow(new AccountException()).when(mockServices).withdraw(anyString(), anyInt());
//
//		try {
//			sibs.transfer(sourceIban, targetIbanBANKEQUAL, amount);
//			fail();
//		} catch (AccountException e) {
//			verify(mockServices).withdraw(sourceIban, amount);
//			verify(mockServices, never()).deposit(targetIbanBANKEQUAL, amount);
//			assertEquals(0, sibs.getNumberOfOperations());
//		}
//
//	}
//
//	@Test // Question 6
//	public void testFailDeposit() throws SibsException, OperationException, AccountException {
//
//		doThrow(new AccountException()).when(mockServices).deposit(targetIbanBANKDIFERENT, amount);
//
//		try {
//			sibs.transfer(sourceIban, targetIbanBANKDIFERENT, amount);
//			fail();
//		} catch (AccountException e) {
//			verify(mockServices).withdraw(sourceIban, 106);
//			verify(mockServices).deposit(targetIbanBANKDIFERENT, amount);
//			verify(mockServices).deposit(sourceIban, 106);
//			assertEquals(0, sibs.getNumberOfOperations());
//		}
//
//	}
//
//	@After
//	public void tearDown() {
//		mockServices = null;
//		sibs = null;
//	}

}
