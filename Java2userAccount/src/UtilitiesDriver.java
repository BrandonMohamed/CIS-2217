
public class UtilitiesDriver {
	public static <E extends Comparable<E>> void print(E[] list) {
		if (list.length == 0)
			return;
		int i;
		System.out.print("[");
		for (i = 0; i < list.length - 1; i++) {
			System.out.print(list[i] + ", ");
		}
		System.out.println(list[i] + "]");
	}

	public static <E extends Comparable<E>> void shuffler(E[] list) {
		for (int i = 0; i < list.length; i++) {
			int j = (int) (Math.random() * list.length);
			E temp = list[i];
			list[i] = list[j];
			list[j] = temp;
		}
	}

	public static void main(String[] args) {

		System.out.println("\tINSERTION SORT");

		System.out.println("Integer test: ");
		Integer intList[] = { 3333, 40, 8, 122, 18134, -1 };
		System.out.print("Before: ");
		print(intList);
		Utilities.insertionSort(intList);
		System.out.print("After:  ");
		print(intList);

		System.out.println("\nString test:");
		String stringList[] = { "Panda", "Cat", "Dog", "Bird", "Fish" };
		System.out.print("Before: ");
		print(stringList);
		Utilities.insertionSort(stringList);
		System.out.print("After:  ");
		print(stringList);

		System.out.println("\nFacebookUser test:");
		FacebookUser fbList[] = { new FacebookUser("Tim", "pass"), new FacebookUser("Mike", "123"),
				new FacebookUser("Brandon", "pword"), new FacebookUser("Mohamed", "456"),
				new FacebookUser("Andrew", "word") };
		System.out.print("Before: ");
		print(fbList);
		Utilities.insertionSort(fbList);
		System.out.print("After:  ");
		print(fbList);

		System.out.println("\n\t SHUFFLING ARRAYS\n");
		shuffler(intList);
		shuffler(stringList);
		shuffler(fbList);
		System.out.println("Arrays now shuffled:");

		System.out.println("\n\t  QUICK SORT");
		System.out.println("Integer test:");
		System.out.print("Before: ");
		print(intList);
		Utilities.quickSort(intList);
		System.out.print("After:  ");
		print(intList);

		System.out.println("\nString test:");
		System.out.print("Before: ");
		print(stringList);
		Utilities.quickSort(stringList);
		System.out.print("After:  ");
		print(stringList);

		System.out.println("\nFacebookUser test:");
		System.out.print("Before: ");
		print(fbList);
		Utilities.quickSort(fbList);
		System.out.print("After:  ");
		print(fbList);

	}

}
