package edu.icbt.las.labappointmentsystem.controller;

import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.Test;
import edu.icbt.las.labappointmentsystem.dto.AllTestResponse;
import edu.icbt.las.labappointmentsystem.dto.common.ErrorResponse;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/tests")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;
    @GetMapping("/")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity getAllTests() {
        try {
            log.info("get test ..");
            return ResponseEntity.ok(getAllTestMap(testService.findAll().stream().filter(test -> test.getStatus().equals(EntityBase.Status.ACTIVE))));
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private List<AllTestResponse> getAllTestMap(Stream<Test> testStream) {
        List<AllTestResponse> responses = new ArrayList<>();
        testStream.forEach(test -> {
            responses.add(AllTestResponse.builder()
                    .id(test.getId())
                    .testPrice(test.getPrice())
                    .testName(test.getName())
                    .testShortName(test.getShortName())
                    .labName(test.getLab().getName())
                    .labNumber(test.getLab().getNumber())
                    .build());
        });
        return responses;
    }
}