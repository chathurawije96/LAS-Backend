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
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

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
    @Transactional(rollbackFor=ServiceException.class)
    public @NotBlank String makeAppointment(MakeAppointmentRequest makeAppointmentRequest, String loggedUser) throws ServiceException {

        Date appointmentDate = null;
        try {
            appointmentDate = DATE_FORMAT.parse(makeAppointmentRequest.getAppointmentDate());
        } catch (ParseException e) {
            throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"Invalid Date Format..");
        }
        int dayAppointmentCount = appointmentValidations(makeAppointmentRequest, loggedUser, appointmentDate);
        Appointment appointment = Appointment.builder().appointmentDate(appointmentDate)
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
        serviceChargetotalPrice = serviceChargetotalPrice.setScale(2, RoundingMode.HALF_EVEN);
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
        return appointment.getAppointmentNumber();
    }

    private Date getAppointmentTime(int dayAppointmentCount) {
        try {
            LocalTime time = LocalTime.parse("08:30:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (dayAppointmentCount>0){
                time = time.plusMinutes(10L *dayAppointmentCount);
            }
            return TIME_FORMAT.parse(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (ParseException e) {
            log.error("Invalid Time format..");
            throw new RuntimeException(e);
        }
    }

    private int appointmentValidations(MakeAppointmentRequest makeAppointmentRequest, String loggedUser, Date appointmentDate) throws ServiceException{
        try {
            List<Appointment> dayAppointment = appointmentRepository.findAllByAppointmentDateAndStatus(appointmentDate, EntityBase.Status.ACTIVE);
            if (dayAppointment.size() == dailyAppointmentCount){
                throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"Daily appointment count exceeded..");
            }
            List<Appointment> dayUserAppointment = appointmentRepository.findAllByAppointmentDateAndUser_usernameAndStatus(appointmentDate,
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
