import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Utilities {
	public final Comparator<String> BY_STRING_IGNORECASE = new string_IgnoreCase();
	public final Comparator<Integer> BY_INTEGER_REVERSE = new int_Reverse();

	public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
		if (list.size() == 0)
			return list;

		ArrayList<E> tmpList = new ArrayList<>();
		for (E i : list) {
			if (!tmpList.contains(i))
				tmpList.add(i);
		}
		return tmpList;

	}

	private static class int_Reverse implements Comparator<Integer> {

		@Override
		public int compare(Integer int1, Integer int2) {

			return int2.compareTo(int1);
		}

	}

	private static class string_IgnoreCase implements Comparator<String> {
		@Override

		public int compare(String string1, String string2) {

			return string1.compareToIgnoreCase(string2);
		}
	}

	public static File getFile() throws IOException {

		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			throw new IOException("Cancelled, exiting program");
		}

	}

	// Message PopUp
	public static void showMessage(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);

	}

	// Message PopUp
	public static void showError(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);

	}
}
