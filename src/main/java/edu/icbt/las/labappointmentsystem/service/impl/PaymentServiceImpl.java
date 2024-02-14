package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.Payment;
import edu.icbt.las.labappointmentsystem.repository.PaymentRepository;
import edu.icbt.las.labappointmentsystem.service.PaymentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment,Long> implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @PostConstruct
    void init(){
        init(paymentRepository);
    }
}
