package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.Report;
import edu.icbt.las.labappointmentsystem.repository.ReportRepository;
import edu.icbt.las.labappointmentsystem.service.ReportService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends GenericServiceImpl<Report,Long> implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @PostConstruct
    void init(){
        init(reportRepository);
    }
}
