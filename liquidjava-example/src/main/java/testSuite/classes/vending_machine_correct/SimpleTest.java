package testSuite.classes.vending_machine_correct;

public class SimpleTest {
	public static void main(String[] args) {
		VendingMachine vm = new VendingMachine(); // 30 cents to buy
		vm.insertTenCents();
		vm.insertTenCents();
		vm.insertTenCents();
		vm.buy();
	}
}