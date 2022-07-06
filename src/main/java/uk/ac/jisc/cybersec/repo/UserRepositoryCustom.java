
package uk.ac.jisc.cybersec.repo;

import java.util.List;

import org.springframework.data.repository.query.Param;

import uk.ac.jisc.cybersec.model.User;

public interface UserRepositoryCustom {

    void updateUser(@Param("username") final String username, @Param("id") final long id);

    List<User> searchForUsers(@Param("username") final String username);

}
