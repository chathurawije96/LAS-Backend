package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.Lab;
import edu.icbt.las.labappointmentsystem.repository.LabRepository;
import edu.icbt.las.labappointmentsystem.service.LabService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabServiceImpl extends GenericServiceImpl<Lab,Long> implements LabService {
    @Autowired
    private LabRepository labRepository;

    @PostConstruct
    void init(){
        init(labRepository);
    }
}
