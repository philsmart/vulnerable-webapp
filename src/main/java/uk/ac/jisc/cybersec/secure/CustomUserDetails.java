
package uk.ac.jisc.cybersec.secure;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private static final long serialVersionUID = 718320060706738788L;
    
    private final uk.ac.jisc.cybersec.model.User user;

    public CustomUserDetails(final String username, final String password, final boolean enabled,
            final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
            final Collection<? extends GrantedAuthority> authorities, uk.ac.jisc.cybersec.model.User user) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;

    }

    /**
     * @return Returns the user.
     */
    public uk.ac.jisc.cybersec.model.User getUser() {
        return user;
    }
}
