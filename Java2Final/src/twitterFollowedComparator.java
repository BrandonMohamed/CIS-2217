import java.io.Serializable;
import java.util.Comparator;

public class twitterFollowedComparator implements Comparator<TwitterUser>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6512694656375870181L;

	@Override
	public int compare(TwitterUser o1, TwitterUser o2) {

		return o1.getFollowedCount() - o2.getFollowedCount();
	}

}