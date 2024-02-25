package edu.icbt.las.labappointmentsystem.service;

import edu.icbt.las.labappointmentsystem.domain.Payment;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;


public interface PaymentService extends GenericService<Payment,Long> {

    Payment findByAppointmentId(Long id) throws ServiceException;
}
