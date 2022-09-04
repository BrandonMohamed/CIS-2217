import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class Facebook implements Serializable {
	private static final long serialVersionUID = 7099795459621169470L;
	private ArrayList<FacebookUser> users = new ArrayList<FacebookUser>(); // Arraylist to hold all users
	GenericStack<FacebookUndo> undoList = new GenericStack<FacebookUndo>();

	// Constructor
	public Facebook() {

	}

	public Facebook(ArrayList<FacebookUser> list) {
		users.addAll(list);
	}

	// EXPLANATION
	/*
	 * Requirements were to have a list of undo actions that are processed last
	 * in/last out. Method to undo the last action in Undo Stack.
	 */
	String undo() throws RuntimeException {

		String msgToReturn;

		// Throw exception if list is empty
		if (this.undoList.getSize() == 0)
			throw new RuntimeException("Error: No undo actions available");

		try {
			FacebookUndo undoObj = this.undoList.pop(); // get last element from stack
			int listSize = this.undoList.getSize();
			switch (undoObj.undoAction) {
			case "deleteUser": // Undo deletions
				this.addUser(undoObj.undoObj);
				msgToReturn = "Reverted Action: " + undoObj.undoAction + ", on user: " + undoObj.undoObj.toString();
				break;
			case "addUser": // undo Additions
				this.users.remove(undoObj.undoObj);
				msgToReturn = "Reverted Action: " + undoObj.undoAction + ", on user: " + undoObj.undoObj.toString();
				break;
			case "friend": // undo Additions
				undoObj.sourceObj.defriend(undoObj.undoObj);
				msgToReturn = "Reverted Action: " + undoObj.undoAction + ", on user: " + undoObj.undoObj.toString();
				break;
			case "deFriend": // undo Additions
				undoObj.sourceObj.friend(undoObj.undoObj);
				msgToReturn = "Reverted Action: " + undoObj.undoAction + ", on user: " + undoObj.undoObj.toString();
				break;
			default:
				throw new RuntimeException("Error: Invalid undo action");

			}
			if (this.undoList.getSize() > listSize)
				this.undoList.pop(); // Do not want actions initiated by undo method to add to undo list
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}

		return msgToReturn;
	}

	// Add new user to facebook
	void addUser(String userName, String Password, String passwordHint) throws RuntimeException {
		FacebookUser tmpUser = new FacebookUser(userName, Password);
		tmpUser.setPasswordHint(passwordHint);
		this.users.add(tmpUser);
		Collections.sort(this.users);
		FacebookUndo tmpUndo = new FacebookUndo("addUser", tmpUser);
		this.undoList.push(tmpUndo); // add to undo list

	}

	// Add new user to facebook
	void addUser(FacebookUser tmpUser) throws RuntimeException {
		if (this.users.contains(tmpUser))
			throw new RuntimeException("Error: User already exists");
		this.users.add(tmpUser);
		Collections.sort(this.users);
		this.undoList.push(new FacebookUndo("addUser", null, tmpUser));

	}

	// Delete User from facebook
	void deleteUser(String userName) throws RuntimeException {

		FacebookUser tmpUser = searchUser(userName);
		if (tmpUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}
		this.users.remove(tmpUser);
		FacebookUndo tmpUndo = new FacebookUndo("deleteUser", null, tmpUser);
		this.undoList.push(tmpUndo);

	}

	// Delete User from facebook
	void deleteUser(FacebookUser tmpUser) throws RuntimeException {

		if (this.users.contains(tmpUser))
			this.users.remove(tmpUser);
		else
			throw new RuntimeException("Error: User " + tmpUser.toString() + " does not exist");

		this.undoList.push(new FacebookUndo("deleteUser", null, tmpUser));
	}

	// Search for user. Return null if not found
	private FacebookUser searchUser(String userName) {

		for (FacebookUser i : this.users) {
			// System.out.println(i);
			if (i.getUsername().equalsIgnoreCase(userName))
				return i;
		}
		return null;
	}

	// Return copy of Users arraylist
	ArrayList<FacebookUser> listUsers() throws CloneNotSupportedException {
		ArrayList<FacebookUser> tmpUsers = new ArrayList<>();
		for (FacebookUser i : this.users) {
			tmpUsers.add(i.clone());
		}

		return tmpUsers;

	}

	// returns password hint of user
	String getPasswordHint(String userName) throws RuntimeException {
		FacebookUser tmpUser = searchUser(userName);
		if (tmpUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}
		return tmpUser.getPasswordHelp();
	}

	// Add user to friends list
	void friend(String userName, String usrFriend) throws RuntimeException {
		FacebookUser baseUser = searchUser(userName);
		if (baseUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}

		FacebookUser friendUser = searchUser(usrFriend);
		if (friendUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}

		baseUser.friend(friendUser);
		this.undoList.push(new FacebookUndo("friend", baseUser, friendUser));

	}

	// Defriend user
	void deFriend(String userName, String usrFriend) throws RuntimeException {
		FacebookUser baseUser = searchUser(userName);
		if (baseUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}

		FacebookUser friendUser = searchUser(usrFriend);
		if (friendUser == null) {
			throw new RuntimeException("Error: User " + userName + " does not exist");
		}

		baseUser.defriend(friendUser);
		this.undoList.push(new FacebookUndo("deFriend", baseUser, friendUser));
	}

	ArrayList<FacebookUser> getFriends(String userName) throws RuntimeException, CloneNotSupportedException {
		FacebookUser tmpUser = searchUser(userName);
		if (tmpUser == null)
			throw new RuntimeException("Error: User " + userName + " does not exist");
		return tmpUser.getFriends();

	}

	ArrayList<FacebookUser> recommendFriends(String userName) throws RuntimeException {
		FacebookUser tmpUser = searchUser(userName);
		if (tmpUser == null)
			throw new RuntimeException("Error: User " + userName + " does not exist");
		ArrayList<FacebookUser> listToReturn = new ArrayList<FacebookUser>();
		ArrayList<FacebookUser> processedUsers = new ArrayList<FacebookUser>();
		listToReturn = recurseFriends(tmpUser, listToReturn, processedUsers, tmpUser);
		return listToReturn;

	}

	ArrayList<FacebookUser> recurseFriends(FacebookUser baseUser, ArrayList<FacebookUser> listToReturn,
			ArrayList<FacebookUser> processedUsers, FacebookUser tmpUser) {

		if (processedUsers.contains(tmpUser))
			return listToReturn;
		processedUsers.add(tmpUser);

		for (FacebookUser i : tmpUser.friends) {
			// Add to list if it does not already exist and is not the initial user we are
			// making recommendation for
			if (baseUser != i && !listToReturn.contains(i))
				listToReturn.add(i);
			// Recurse friends friends, if not already processed
			if (!processedUsers.contains(i))
				recurseFriends(tmpUser, listToReturn, processedUsers, tmpUser);
		}

		return listToReturn;

	}

	void login(String username, String password) throws RuntimeException {

		FacebookUser tmpUser = this.binarySearch(username);
		if (tmpUser == null)
			throw new RuntimeException("Invalid Username");

		if (!tmpUser.checkPassword(password))
			throw new RuntimeException("Invalid password entered");

	}

	// binary search of facebook users array, using binary search
	FacebookUser binarySearch(String x) {
		int left = 0;
		int right = this.users.size() - 1;
		while (left <= right) {
			int middle = left + (right - left) / 2;
			FacebookUser tmpUser = this.users.get(middle);
			// Found user
			if (tmpUser.getUsername() == x)
				return tmpUser;

			// split into right half
			if (tmpUser.getUsername().compareTo(x) < 0)
				left = middle++;

			// split into left half
			else
				right = middle--;
		}

		return null;
	}

	void like(String username, String strToLike) throws RuntimeException {
		FacebookUser tmpUser = this.binarySearch(username);
		if (tmpUser == null)
			throw new RuntimeException("Invalid Username");

		tmpUser.like(strToLike);

	}

	/*
	 * EXPLANATION: I chose TreeSet for its speed, efficiency, sorting, and it won't
	 * allow duplicates. This will provide a duplicate-free set no matter how many
	 * times someone likes the same things.
	 */

	TreeSet<String> getLikes(String username) throws RuntimeException {
		FacebookUser tmpUser = this.binarySearch(username);
		if (tmpUser == null)
			throw new RuntimeException("Invalid Username");

		return tmpUser.getLikes();

	}

}

class FacebookUndo {
	String undoAction; // Undo action to perform
	FacebookUser sourceObj; // Object to initate undo action against
	FacebookUser undoObj; // Object that is being added/removed back to sourceObj

	// Constructors
	FacebookUndo() {

	}

	FacebookUndo(String undoAction, FacebookUser sourceObj, FacebookUser undoObj) {
		this.undoAction = undoAction;
		this.sourceObj = sourceObj;
		this.undoObj = undoObj;
	}

	FacebookUndo(String undoAction, FacebookUser undoObj) {
		this.undoAction = undoAction;

		this.undoObj = undoObj;
	}
}