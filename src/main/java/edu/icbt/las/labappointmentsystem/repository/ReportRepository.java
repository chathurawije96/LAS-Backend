package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report,Long> {
}
