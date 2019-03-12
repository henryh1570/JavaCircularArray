package ppkg;

public class Driver {

	public static void main(String[] args) {
		System.out.println("Hello Worlds!");
		CircularArray<Integer> list = new CircularArray<Integer>(5);
		list.addElement(1);
		list.addElement(2);
		list.addElement(3);
		list.addElement(4);
		list.addElement(5);
		System.out.println(list.toStringState());
		System.out.println(list.toString());
		
		list.removeElement(1);
		System.out.println(list.toStringState());
		System.out.println(list.toString());
		
	}
}
