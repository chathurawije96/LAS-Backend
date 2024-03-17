package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.Report;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report,Long> {

    Report findFirstByAppointmentTests_IdAndStatus(Long appointmentTestId, EntityBase.Status status) throws DataAccessException;
}
