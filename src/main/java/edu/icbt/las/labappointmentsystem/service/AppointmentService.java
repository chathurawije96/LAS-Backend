package edu.icbt.las.labappointmentsystem.service;

import edu.icbt.las.labappointmentsystem.domain.Appointment;
import edu.icbt.las.labappointmentsystem.dto.MakeAppointmentRequest;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;

import java.util.List;


public interface AppointmentService extends GenericService<Appointment,Long> {
    List<Appointment> getAllByLoggedUser(String username) throws ServiceException;

    Appointment makeAppointment(MakeAppointmentRequest makeAppointmentRequest, String loggedUser) throws ServiceException;
}
