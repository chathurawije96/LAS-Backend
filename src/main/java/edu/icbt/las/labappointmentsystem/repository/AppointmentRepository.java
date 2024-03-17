package edu.icbt.las.labappointmentsystem.repository;

import edu.icbt.las.labappointmentsystem.domain.Appointment;
import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment,Long> {

    List<Appointment> findAllByUser_usernameOrderByCreatedAtDesc(String username) throws DataAccessException;

    List<Appointment> findAllByAppointmentDateAndStatus(Date appointmentDate, EntityBase.Status status) throws DataAccessException;
    List<Appointment> findAllByAppointmentDateAndUser_usernameAndStatus(Date appointmentDate,String username, EntityBase.Status status) throws DataAccessException;
}
