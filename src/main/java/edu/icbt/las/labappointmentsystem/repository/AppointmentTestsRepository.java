package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.AppointmentTests;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTestsRepository extends CrudRepository<AppointmentTests,Long> {
}
