package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.Test;
import edu.icbt.las.labappointmentsystem.repository.TestRepository;
import edu.icbt.las.labappointmentsystem.service.TestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends GenericServiceImpl<Test, Long> implements TestService {
    @Autowired
    private TestRepository testRepository;

    @PostConstruct
    void init(){
        init(testRepository);
    }
}
