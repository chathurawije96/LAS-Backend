package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<Test, Long> {
}
