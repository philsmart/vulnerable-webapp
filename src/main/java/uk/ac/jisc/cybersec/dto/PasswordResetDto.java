
package uk.ac.jisc.cybersec.dto;

public class PasswordResetDto {

    private String username;

    private String newPassword;

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return Returns the newPassword.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword The newPassword to set.
     */
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

}
