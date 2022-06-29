
package uk.ac.jisc.cybersec.repo;

import org.springframework.data.repository.query.Param;

public interface UserRepositoryCustom {

    void updateUser(@Param("username") final String username, @Param("id") final long id);

}
