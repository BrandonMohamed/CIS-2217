import java.util.Comparator;

public class facebookFriendsComparator implements Comparator<FacebookUser> {

	@Override
	public int compare(FacebookUser o1, FacebookUser o2) {
		if (o1.friends.size() < o2.friends.size()) {
			return -1;
		} else if (o1.friends.size() > o2.friends.size()) {
			return 1;
		} else {
			return 0;
		}
	}

}
