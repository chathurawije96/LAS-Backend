package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.Appointment;
import edu.icbt.las.labappointmentsystem.repository.AppointmentRepository;
import edu.icbt.las.labappointmentsystem.service.AppointmentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment,Long> implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostConstruct
    void init(){
        init(appointmentRepository);
    }
}
