package ppkg;

public class Driver {

	static CircularArray<Integer> list = new CircularArray<Integer>(4, 2);

	public static void main(String[] args) {
		printState();
		list.add(1);
		printState();
		list.add(2);
		printState();
		list.add(3);
		printState();
		list.add(4);
		printState();
		list.remove(1);
		printState();
		list.add(5);
		printState();
		list.add(9);
		printState();
		list.remove(1);
		printState();
		list.remove(0);
		printState();
		list.remove(0);
		printState();
		list.add(8);
		printState();
		list.remove(0);
		printState();
		list.remove(0);
		printState();
		list.remove(0);
		printState();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		printState();
		list.add(9);
		printState();
		list.clear();
		printState();
	}

	public static void printState() {
		System.out.println(list.toStringState());
		System.out.println(list.toString());
		System.out.println("-------");
	}
}
