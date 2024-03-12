package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.AppointmentTests;
import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.Report;
import edu.icbt.las.labappointmentsystem.dto.UploadReportRequest;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.repository.ReportRepository;
import edu.icbt.las.labappointmentsystem.service.AppointmentTestsService;
import edu.icbt.las.labappointmentsystem.service.ReportService;
import edu.icbt.las.labappointmentsystem.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ReportServiceImpl extends GenericServiceImpl<Report, Long> implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private AppointmentTestsService appointmentTestsService;
    @Autowired
    private UserService userService;

    @PostConstruct
    void init() {
        init(reportRepository);
    }

    @Override
    public void uploadReport(Long appointmentTestId, UploadReportRequest request) throws ServiceException {
        Optional<AppointmentTests> testsOptional = appointmentTestsService.findById(appointmentTestId);
        if (testsOptional.isPresent()) {
            this.save(Report.builder().report(request.getReport())
                    .reportGeneratedUser(userService.findUserByEmail(getLoggedUser()))
                    .createdAt(new Date())
                    .status(EntityBase.Status.ACTIVE)
                    .updatedAt(new Date())
                    .appointmentTests(testsOptional.get())
                    .build());
        }
    }

    @Override
    public Report findReportByAppointmentTest(Long appointmentTestId) throws ServiceException {
        try {
            return reportRepository.findFirstByAppointmentTests_IdAndStatus(appointmentTestId, EntityBase.Status.ACTIVE);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    private String getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return "pub";
        }

        String user = null;
        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            user = details.getUsername();
        } else {
            user = String.valueOf(auth.getPrincipal());
        }
        return user;
    }
}
