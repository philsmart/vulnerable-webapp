
package uk.ac.jisc.cybersec.secure;

public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor.
     *
     */
    public UserNotFoundException() {
        super();

    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public UserNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public UserNotFoundException(final String message, final Throwable cause) {
        super(message, cause);

    }

    /**
     * Constructor.
     *
     * @param message
     */
    public UserNotFoundException(final String message) {
        super(message);

    }

    /**
     * Constructor.
     *
     * @param cause
     */
    public UserNotFoundException(final Throwable cause) {
        super(cause);

    }

    /**
     * 
     */
    private static final long serialVersionUID = -102432034997511778L;

}
