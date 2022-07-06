
package uk.ac.jisc.cybersec.repo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.jisc.cybersec.model.User;

/**
 * A custom repository which is vulnerable to SQL injection
 */
@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryCustomImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void updateUser(final String username, final long id) {
        final Query query =
                entityManager.createNativeQuery("UPDATE User set username='" + username + "' where id=" + id);
        query.executeUpdate();

    }

    @Override
    public List<User> searchForUsers(final String username) {
        final Query query = entityManager.createNativeQuery(
                "SELECT id, username, enabled, password from User where username='" + username + "'");
        final List<?> result = query.getResultList();
        return convertToUser(result);

    }

    private List<User> convertToUser(final List<?> results) {

        final List<User> usersConverted = new ArrayList<>(results.size());
        for (final Object result : results) {
            log.debug("Raw result: {}", result);
            if (result instanceof Object[]) {
                final Object[] resultArray = (Object[]) result;
                final User user = new User();
                if (resultArray[0] instanceof BigInteger) {
                    user.setId(((BigInteger) resultArray[0]).longValue());
                }
                user.setUsername(((String) resultArray[1]));
                if (resultArray[2] instanceof Boolean) {
                    user.setEnabled(((Boolean) resultArray[2]));
                }
                user.setPassword(((String) resultArray[3]));
                usersConverted.add(user);
            }

        }
        return usersConverted;
    }

}
