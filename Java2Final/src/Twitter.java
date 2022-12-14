import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Changes since MidTerm TwitterUser: Converted from ArrayList to Hashmap for
 * followed list. Added new HashMap to store followers. Used hashmap as get and
 * put operations are O(1) Changed getFollowed method to return a collection
 * instead of arrayList
 * 
 * twitterFollowerComparator: New comparator to use with sort operations. Sort
 * Criteria: 1. Number of followers (largest to smallest) 2. If two users have
 * the same number of followers, sort by the number of people that user is
 * following (largest to smallest) 3. If two users have the same number of
 * followers and are following the same number of people, sort by user id
 * (smallest to largest)
 * 
 *
 */

public class Twitter implements Cloneable {

	ArrayList<TwitterUser> twitterUsers = new ArrayList<TwitterUser>(); // List of twitter accounts and data, sorted
	private String dbFile = "social_network.edgelist"; // space delimited text file of twitter data. Can be overridden
														// when constructor called

	// Constructors
	public Twitter() {

	}

	public Twitter(String fileName) {
		this.dbFile = fileName;
	}

	ArrayList<TwitterUser> getList() {
		return (ArrayList<TwitterUser>) this.twitterUsers.clone();
	}

//Read in twitter data and build list of twitterUsers
	void loadDB() throws IOException, RuntimeException {

		File twitterFile = new File(this.dbFile);
		if (!twitterFile.exists()) {
			Utilities.showMessage(
					"Unable to find file, " + this.dbFile
							+ ", in current working directory.  Please select the file to use",
					"Unable to find " + this.dbFile);

			try {
				twitterFile = Utilities.getFile();
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}

		// Parse Data
		// Build Regex
		String twitterRegex = "^(?<userid>\\d+)\\s+(?<followed>\\d+)$";
		Pattern p = Pattern.compile(twitterRegex);

		// variables
		String lineRead; // used by bufferedreader
		TwitterUser curUser = null; // placeholder for current user being processed
		TwitterUser followedUser = null; // placeHolder for user to follow
		HashMap<Integer, TwitterUser> twitterMap = new HashMap<>(); // Use HashMap to easily store/retrieve values while
																	// loading flat file

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(twitterFile));) {
			int count = 0;
			while ((lineRead = bufferedReader.readLine()) != null) {
				count++;
				Matcher m = p.matcher(lineRead);

				if (m.matches()) {
					int userID = Integer.parseInt(m.group("userid"));
					int userID2 = Integer.parseInt(m.group("followed"));

					curUser = twitterMap.get(userID);
					if (curUser == null) {
						curUser = new TwitterUser(userID);
						twitterMap.put(userID, curUser);
					}

					followedUser = twitterMap.get(userID2);
					if (followedUser == null) {
						followedUser = new TwitterUser(userID2);
						twitterMap.put(userID2, followedUser);
					}

					// Follow User
					if (!curUser.isFollowed(followedUser))
						curUser.follow(followedUser);

					// Add Follower
					followedUser.followers.put(curUser.getUserID(), curUser);

				} else {
					throw new RuntimeException("Malformed data found at line " + count + " in twitter edgelist");
				}

			}
			// Add hashmap values to arraylist and sort
			this.twitterUsers.addAll(twitterMap.values());
			HeapSort.fromList(this.twitterUsers);
			HeapSort.fromList(this.twitterUsers, new twitterFollowersComparator()); // Sort

			Collections.sort(this.twitterUsers, new twitterFollowersComparator()); // Sort by popularity
		} catch (

		IOException e) {
			throw new IOException(e.getMessage());
		}

	}

	// Recursive function to get all related twitter users, based on followed users.
	ArrayList<TwitterUser> getNeighborhood(int userID, int maxDepth)
			throws RuntimeException, CloneNotSupportedException {
		TwitterUser tmpUser = binarySearch(userID);
		if (tmpUser == null)
			throw new RuntimeException("Error: User " + userID + " does not exist");

		if (tmpUser.getFollowedCount() == 0)
			throw new RuntimeException(tmpUser.getUserID() + " does not follow anyone");

		ArrayList<TwitterUser> listToReturn = new ArrayList<TwitterUser>();

		listToReturn = getNeighborhood(tmpUser, listToReturn, tmpUser, 0, maxDepth);
		return listToReturn;

	}

	// Recursive function to get all related twitter users, based on followed users.
	ArrayList<TwitterUser> getNeighborhood(TwitterUser tmpUser, int maxDepth)
			throws RuntimeException, CloneNotSupportedException {

		if (tmpUser == null)
			throw new RuntimeException("Error: User " + tmpUser + " does not exist");

		if (tmpUser.getFollowedCount() == 0)
			throw new RuntimeException(tmpUser.getUserID() + " does not follow anyone");

		ArrayList<TwitterUser> listToReturn = new ArrayList<TwitterUser>();

		listToReturn = getNeighborhood(tmpUser, listToReturn, tmpUser, 0, maxDepth);
		return listToReturn;

	}

	// Recursive function to get all related twitter users, based on followed users.
	ArrayList<TwitterUser> getNeighborhood(TwitterUser baseUser, ArrayList<TwitterUser> listToReturn,
			TwitterUser tmpUser, int depth, int maxDepth) throws CloneNotSupportedException {
		for (TwitterUser i : tmpUser.getFollowed()) {
			if (!listToReturn.contains(i)) {
				listToReturn.add(i);
				if (depth < maxDepth)
					getNeighborhood(baseUser, listToReturn, i, ++depth, maxDepth);
			}

		}

		return listToReturn;

	}

	// binary search of twitteruser array, using binary search
	TwitterUser binarySearch(int x) throws CloneNotSupportedException {
		int left = 0;
		int right = this.twitterUsers.size() - 1;
		while (left <= right) {
			int middle = left + (right - left) / 2;
			TwitterUser tmpUser = this.twitterUsers.get(middle);
			// Found user
			if (tmpUser.getUserID() == x)
				return tmpUser;

			// split into right half
			if (tmpUser.getUserID() < x)
				left = middle++;

			// split into left half
			else
				right = middle--;
		}

		return null;
	}

	// Simple linear search, returns index number
	Integer linearSearch(int x) {
		for (int i = 0; i < this.twitterUsers.size(); i++) {
			if (this.twitterUsers.get(i).getUserID() == x)
				return x;

		}
		return -1;

	}

	// Return followers of user
	Collection<TwitterUser> getFollowing(TwitterUser user) throws CloneNotSupportedException {
		return user.getFollowing();

	}

	// return twitter user, sorted by popularity

	TwitterUser getByPopularity(int x) {
		return this.twitterUsers.get(x);

	}

}
