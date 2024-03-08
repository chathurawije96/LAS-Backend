package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.Payment;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
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

    @Override
    public Payment findByAppointmentId(Long id) throws ServiceException {
        try {
            return paymentRepository.findByAppointment_Id(id);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    @Override
    public void savePaymentSuccess(Long id) throws ServiceException {
        try {
            Payment payment = paymentRepository.findByAppointment_Id(id);
            if (payment != null) {
                payment.setStatus(EntityBase.Status.ACTIVE);
                paymentRepository.save(payment);
            }
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }
}
