import java.io.Serializable;
import java.util.Objects;



abstract public class UserAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8611266401890159805L;
	// Variables
	private String username; // Username
	private String password; // Password
	private boolean active; // Indicates whether account is active

	// Constructor
	UserAccount(String username, String password) {
		this.username = username;
		this.password = password;
		this.active = true;
		;
	}

	UserAccount() {
	}

	// Checks argument against account password
	public boolean checkPassword(String password) {
		if (this.password.equals(password))
			return true;

		return false;
	}

	// Marks account as disabled
	public void deactiveAccount() {
		this.active = false;
	}

	public String toString() {
		return this.username;
	}

	// Getter Methods
	public boolean isActive() {
		return active;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		UserAccount other = (UserAccount) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public abstract String getPasswordHelp();

}