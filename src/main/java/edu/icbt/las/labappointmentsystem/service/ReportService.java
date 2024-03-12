package edu.icbt.las.labappointmentsystem.service;

import edu.icbt.las.labappointmentsystem.domain.Report;
import edu.icbt.las.labappointmentsystem.dto.UploadReportRequest;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;


public interface ReportService extends GenericService<Report,Long> {

    public void uploadReport(Long appointmentTestId, UploadReportRequest request) throws ServiceException;

    public Report findReportByAppointmentTest(Long appointmentTestId) throws ServiceException;
}
