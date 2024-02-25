package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.*;
import edu.icbt.las.labappointmentsystem.dto.AppointmentTestRequest;
import edu.icbt.las.labappointmentsystem.dto.MakeAppointmentRequest;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.exception.ServiceExceptionType;
import edu.icbt.las.labappointmentsystem.repository.AppointmentRepository;
import edu.icbt.las.labappointmentsystem.service.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment,Long> implements AppointmentService {
    @Value("${system.daily.appointment.count}")
    private int dailyAppointmentCount;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TestService testService;
    @Autowired
    private AppointmentTestsService appointmentTestsService;
    @Autowired
    private PaymentService paymentService;

    @PostConstruct
    void init(){
        init(appointmentRepository);
    }

    @Override
    public List<Appointment> getAllByLoggedUser(String username) throws ServiceException {
        try {
            return appointmentRepository.findAllByUser_usernameOrderByCreatedAtDesc(username);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    @Override
    public Appointment makeAppointment(MakeAppointmentRequest makeAppointmentRequest, String loggedUser) throws ServiceException {
        int dayAppointmentCount = appointmentValidations(makeAppointmentRequest, loggedUser);

        Appointment appointment = Appointment.builder().appointmentDate(makeAppointmentRequest.getAppointmentDate())
                .appointmentNumber(String.format("AP-%05d", dayAppointmentCount + 1))
                .user(userService.findUserByEmail(loggedUser))
                .recommendedDoctor(makeAppointmentRequest.getRecommendedDoctor())
                .createdAt(new Date())
                .status(EntityBase.Status.ACTIVE)
                .updatedAt(new Date())
                .appointmentTime(getAppointmentTime(dayAppointmentCount))
                .build();
        appointment = this.save(appointment);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(AppointmentTestRequest request: makeAppointmentRequest.getTests() ){
            Test test = testService.findById(request.getTestId()).get();
            totalPrice = totalPrice.add(test.getPrice());
            appointmentTestsService.save(AppointmentTests.builder()
                    .appointment(appointment)
                    .createdAt(new Date())
                    .status(EntityBase.Status.PENDING)
                    .updatedAt(new Date())
                    .test(test)
                    .build());
        }
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal serviceChargetotalPrice = new BigDecimal(10).multiply(totalPrice).scaleByPowerOfTen(-2);
        Payment payment = Payment.builder().appointment(appointment)
                .serviceCharge(serviceChargetotalPrice)
                .amount(totalPrice)
                .appointment(appointment)
                .createdAt(new Date())
                .status(EntityBase.Status.PENDING)
                .updatedAt(new Date())
                .totalPay(totalPrice.add(serviceChargetotalPrice))
                .build();
        paymentService.save(payment);
        return appointment;
    }

    private Date getAppointmentTime(int dayAppointmentCount) {

        Locale locale = new Locale("en", "UK");
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);

        try {
            LocalTime time = LocalTime.parse("08:30:00");
            if (dayAppointmentCount>0){
                time = time.plusMinutes(10L *dayAppointmentCount);
            }
            return dateFormat.parse(time.toString());
        } catch (ParseException e) {
            log.error("Invalid Time format..");
            throw new RuntimeException(e);
        }
    }

    private int appointmentValidations(MakeAppointmentRequest makeAppointmentRequest, String loggedUser) throws ServiceException{
        try {
            List<Appointment> dayAppointment = appointmentRepository.findAllByAppointmentDateAndStatus(makeAppointmentRequest.getAppointmentDate(), EntityBase.Status.ACTIVE);
            if (dayAppointment.size() == dailyAppointmentCount){
                throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"Daily appointment count exceeded..");
            }
            List<Appointment> dayUserAppointment = appointmentRepository.findAllByAppointmentDateAndUser_usernameAndStatus(makeAppointmentRequest.getAppointmentDate(),
                    loggedUser,EntityBase.Status.ACTIVE);
            if (!dayUserAppointment.isEmpty()){
                throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"This patient has an active appointment on the selected date");
            }
            return dayAppointment.size();
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }
}
