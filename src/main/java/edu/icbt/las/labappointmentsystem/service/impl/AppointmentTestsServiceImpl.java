package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.AppointmentTests;
import edu.icbt.las.labappointmentsystem.repository.AppointmentTestsRepository;
import edu.icbt.las.labappointmentsystem.service.AppointmentTestsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentTestsServiceImpl extends GenericServiceImpl<AppointmentTests,Long> implements AppointmentTestsService {
    @Autowired
    private AppointmentTestsRepository appointmentTestsRepository;

    @PostConstruct
    void init(){
        init(appointmentTestsRepository);
    }
}
