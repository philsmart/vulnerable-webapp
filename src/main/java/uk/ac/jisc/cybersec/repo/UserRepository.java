
package uk.ac.jisc.cybersec.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.jisc.cybersec.model.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User findByUsername(String username);

}
