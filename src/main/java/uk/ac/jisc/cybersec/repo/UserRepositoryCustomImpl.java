
package uk.ac.jisc.cybersec.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext EntityManager entityManager;

    @Override
    // @Query(value = "update User user set username = :username where id=:id", nativeQuery = true)
    public void updateUser(final String username, final long id) {
        final Query query =
                entityManager.createNativeQuery("UPDATE User set username='" + username + "' where id=" + id);
        query.executeUpdate();

    }

}
