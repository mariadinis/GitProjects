package sibs_mbway;

public class MBwayMethodsTest {

	/* guideline 9: "Automate Tests" */

//	private Client client1, client2;
//	private String iban1, iban2, phoneNumber1, phoneNumber2;
//	private Controller c;
//
//	@Before
//	public void setUp() throws BankException, ClientException, AccountException {
//		c = new Controller();
//		Bank bank = new Bank("CGD");
//
//		client1 = new Client(bank, "Mario", "Jorge", "123456789", "456123789", "Rua", 60);
//		bank.addClient(client1);
//		iban1 = bank.createAccount(AccountType.CHECKING, client1, 100, 0);
//
//		client2 = new Client(bank, "Jorge", "Junior", "123789456", "789456123", "Rua", 60);
//		bank.addClient(client2);
//		iban2 = bank.createAccount(AccountType.CHECKING, client2, 100, 0);
//
//	}
//
//	@Test
//	public void Associate() {
//		c.associate(iban1, "987654321");
//	}
//
//	@Test
//	public void Confirm() {
//		String phoneNumber = client1.getPhoneNumber();
//		MBwayAccount mbwayAccount = MBwayAccount.getMBwayAccountByPhoneNumber(phoneNumber);
//		c.confirm(phoneNumber, mbwayAccount.getCode());
//	}
//
//	@Test
//	public void successTransfer() {
//		c.associate(iban2, "987321654");
//		String phoneNumber = client2.getPhoneNumber();
//		MBwayAccount mbwayAccount = MBwayAccount.getMBwayAccountByPhoneNumber(phoneNumber);
//		c.confirm(phoneNumber, mbwayAccount.getCode());
//
//		transferOperationData data = new transferOperationData(new Services(), "987654321", "987321654", 50);
//		c.transfer(data);
//	}
//
//	@After
//	public void tearDown() {
//		c = null;
//	}
}
