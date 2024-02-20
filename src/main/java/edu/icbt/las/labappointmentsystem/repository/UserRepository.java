package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.User;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmailAndStatus(String email, EntityBase.Status status) throws DataAccessException;
}
