import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;


public class TwitterUser implements Comparable<TwitterUser>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private Integer userID; // Twitter Userid
	HashMap<Integer, TwitterUser> followed = new HashMap<Integer, TwitterUser>(); // Users followed
	HashMap<Integer, TwitterUser> followers = new HashMap<Integer, TwitterUser>(); // Followers of user

	// Constructors
	public TwitterUser() {

	}

	public TwitterUser(Integer userID) {
		this.userID = userID;
	}

	public TwitterUser(Integer userID, HashMap<Integer, TwitterUser> followed) {
		super();
		this.userID = userID;
		this.followed = followed;
	}

	// Return UserID
	Integer getUserID() {
		return userID;
	}

	// Set UserID
	void setUserID(Integer userID) {
		this.userID = userID;
	}

	// Return the number of users followed
	int getFollowedCount() {
		return this.followed.size();
	}

	// Compare by UserID
	@Override
	public int compareTo(TwitterUser usrToCompare) {

		return this.userID - usrToCompare.userID;
	}

	// deep copy clone method
	public TwitterUser clone() throws CloneNotSupportedException {
		TwitterUser tmpUsr = new TwitterUser(this.userID);
		for (TwitterUser userObj : this.followed.values()) {
			tmpUsr.follow(userObj);
		}

		return tmpUsr;

	}

	// Follow twitter user
	void follow(TwitterUser usrObj) {
		if (this.followed.get(usrObj.userID) != null)
			throw new RuntimeException("ERROR: " + usrObj.userID + " already being followed");
		this.followed.put(usrObj.userID, usrObj);
	}

	// Follow twitter user
	void addFollower(TwitterUser usrObj) {
		if (this.followers.get(usrObj.userID) != null)
			throw new RuntimeException("ERROR: " + usrObj.userID + " already being followed");
		this.followers.put(usrObj.userID, usrObj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterUser other = (TwitterUser) obj;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

	// unFollow twitter user
	void unFollow(TwitterUser usrObj) {
		if (this.followed.get(usrObj.userID) == null)
			throw new RuntimeException("ERROR: " + usrObj.userID + " is not being followed");
		this.followed.remove(usrObj.userID);
	}

	// toString method
	public String toString() {
		return this.userID.toString();
	}

//Check if user is already followed
	boolean isFollowed(TwitterUser user) {
		if (this.followed.get(user.userID) != null)
			return true;
		else
			return false;
	}

	Collection<TwitterUser> getFollowed() throws CloneNotSupportedException {

		return this.followed.values();

	}

	Collection<TwitterUser> getFollowing() throws CloneNotSupportedException {

		return this.followers.values();

	}

}
