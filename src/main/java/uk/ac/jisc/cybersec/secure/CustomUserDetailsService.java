
package uk.ac.jisc.cybersec.secure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import uk.ac.jisc.cybersec.model.User;
import uk.ac.jisc.cybersec.repo.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User foundUser = userRepo.findByUsername(username);
        log.debug("Found user [{}] from database", foundUser);
        if (foundUser == null) {
            throw new UserNotFoundException("Could not find username " + username + " in data source");
        }

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (foundUser.getRoles() != null) {
            final Iterator<String> it = foundUser.getRoles().iterator();
            while (it.hasNext()) {

                authorities.add(new SimpleGrantedAuthority(it.next()));
            }
        }
        return new CustomUserDetails(username, foundUser.getPassword(), foundUser.isEnabled(), true, true, true,
                authorities);
    }

}
