package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.Payment;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {

    Payment findByAppointment_Id(Long id) throws DataAccessException;
}
