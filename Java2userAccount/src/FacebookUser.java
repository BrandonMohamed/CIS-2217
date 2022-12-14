import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.TreeSet;

public class FacebookUser extends UserAccount implements Comparable<FacebookUser>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String passwordHint;
	ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
	HashSet<String> likes = new HashSet<String>();

	// Default Constructor
	FacebookUser(String username, String password) {
		super(username, password);

	}

	// Sets Users password hint
	void setPasswordHint(String hint) {
		this.passwordHint = hint;
	}

	// Adds FaceBookUser to users friends list
	void friend(FacebookUser newFriend) throws RuntimeException {
		if (this.friends.contains(newFriend)) {
			throw new RuntimeException("ERROR: " + newFriend.getUsername() + " already exists in friends list");

		}

		this.friends.add(newFriend);
		Collections.sort(this.friends); // Sort friends list every time a friend is added

	}

	// Removes friends from friends list
	void defriend(FacebookUser formerFriend) throws EmptyStackException {
		this.friends.remove(formerFriend);

	}

	// Returns copy of friends list arraylist
	ArrayList<FacebookUser> getFriends() throws CloneNotSupportedException {
		ArrayList<FacebookUser> tmpArr = new ArrayList<FacebookUser>();
		for (FacebookUser userObj : this.friends) {
			tmpArr.add(userObj.clone());
		}

		return tmpArr;
	}

	// Returns users password hint
	@Override
	public String getPasswordHelp() {
		return this.passwordHint;

	}

	// Case Insensitive compare method for FaceBookUser Class
	@Override
	public int compareTo(FacebookUser usrToCompare) {
		return this.getUsername().compareToIgnoreCase(usrToCompare.getUsername());
	}

	// deep copy clone method
	public FacebookUser clone() throws CloneNotSupportedException {
		FacebookUser tmpUsr = new FacebookUser(this.getUsername(), this.getPassword());
		tmpUsr.setPasswordHint(this.passwordHint);
		for (FacebookUser fUser : this.friends) {
			tmpUsr.friend(fUser);
		}

		return tmpUsr;

	}

	void like(String strToLike) {
		this.likes.add(strToLike);

	}

	TreeSet<String> getLikes() {
		return new TreeSet<String>(this.likes);
	}

}
