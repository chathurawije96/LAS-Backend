package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.Lab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends CrudRepository<Lab,Long> {
}
