import java.util.Comparator;

public class facebookNameComparator implements Comparator<FacebookUser> {

	@Override
	public int compare(FacebookUser o1, FacebookUser o2) {
		return o1.getUsername().compareToIgnoreCase(o2.getUsername());
	}

}
