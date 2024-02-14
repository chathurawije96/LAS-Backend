package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.EmailVerification;
import edu.icbt.las.labappointmentsystem.repository.EmailVerificationRepository;
import edu.icbt.las.labappointmentsystem.service.EmailVerificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl extends GenericServiceImpl<EmailVerification,Long> implements EmailVerificationService {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @PostConstruct
    void init(){
        init(emailVerificationRepository);
    }
}
