import java.io.Serializable;
import java.util.Comparator;

public class twitterFollowersComparator implements Comparator<TwitterUser>, Serializable {

	private static final long serialVersionUID = -9206271126384232957L;

	/** Compare Criteria:
	 * 1. Number of followers(Largest to Smallest)
	 * 2.  If number of followers are the same, by number of followed(Largest to Smallest)
	 * 3.  By twitterID(smallest to largest)
	 * 
	 */
	
	@Override
	public int compare(TwitterUser o1, TwitterUser o2) {
		// TODO Auto-generated method stub
		int diff;

		// Sort largest to smallest, based on followers size
		diff = o1.followers.size() - o2.followers.size();
		if (diff > 0)
			return -1;
		else if( diff < 0)
			return 1;

		// sort largest to smallest, based on followed count.
		diff = o1.followed.size() - o2.followed.size();
		if (diff > 0)
			return -1;
		else if( diff < 0)
			return 1;
		
		//sort smallest to largest, based on userid
		diff = o1.getUserID() - o2.getUserID();
		if (diff < 0)
			return -1;
		else if( diff > 0)
			return 1;
		
		
		return 0;

	}

}
