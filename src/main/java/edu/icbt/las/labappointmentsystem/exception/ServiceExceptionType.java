package edu.icbt.las.labappointmentsystem.exception;



import edu.icbt.las.labappointmentsystem.utils.EnumValue;

import java.util.ArrayList;
import java.util.List;


public enum ServiceExceptionType {
    PROCESSING_FAILED(0), AUTHENTICATION_FAILED(1), VALIDATION_FAILED(2), INTEGRATION_FAILED(3), ;

    private int id;

    ServiceExceptionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ServiceExceptionType getById(int id) {
        for(ServiceExceptionType e: ServiceExceptionType.values()) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;// not found
    }

    public static List<EnumValue> getList() {
        List<EnumValue> values = new ArrayList<>();
        for (ServiceExceptionType e : ServiceExceptionType.values()) {
            values.add(new EnumValue(e.getId(), e.getDescription()));
        }
        return values;
    }

    public String getDescription() {
        switch (id) {
            case 0:
                return "Processing Failed";
            case 1:
                return "Authentication Failed";
            case 2:
                return "Validation Failed";
            case 3:
                return "Integration Failed";
            default:
                return "";
        }
    }
}
