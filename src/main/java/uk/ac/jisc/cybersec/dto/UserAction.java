
package uk.ac.jisc.cybersec.dto;

public class UserAction {

    private long id;

    private String username;
    
    private String displayName;

    /**
     * @return Returns the newUsername.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param newUsername The newUsername to set.
     */
    public void setUsername(final String uname) {
        this.username = uname;
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final long id) {
        this.id = id;
    }

	/**
	 * Get the displayName.
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the displayName.
	 *
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
