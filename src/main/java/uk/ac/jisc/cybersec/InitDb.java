
package uk.ac.jisc.cybersec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import uk.ac.jisc.cybersec.model.User;
import uk.ac.jisc.cybersec.repo.UserRepository;

@Component
public class InitDb {

    @Autowired
    UserRepository userRepo;

    private static final Logger log = LoggerFactory.getLogger(InitDb.class);

    @PostConstruct
    public void initDatabase() throws NoSuchAlgorithmException {
        final User admin = new User();
        admin.setUsername("jblogs");
        admin.setEnabled(true);
        admin.setPassword(constructPasswordHash("pa55word"));
        admin.setRoles(Set.of("ROLE_ADMIN"));

        final User user = new User();
        user.setUsername("tjones");
        user.setEnabled(true);
        user.setPassword(constructPasswordHash("newpassword"));
        admin.setRoles(Set.of("ROLE_USER"));

        userRepo.save(admin);
        userRepo.save(user);

    }

    private String constructPasswordHash(final String password) throws NoSuchAlgorithmException {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            final byte[] hash = md.digest();
            final String composedHash = new String(Hex.encode(hash));

            return composedHash;
        } catch (final NoSuchAlgorithmException e) {
            log.error("Could not generate new password hash", e);
            throw e;

        }

    }

}
