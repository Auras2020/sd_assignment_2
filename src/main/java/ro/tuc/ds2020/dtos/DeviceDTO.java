package ro.tuc.ds2020.dtos;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
public class DeviceDTO {

    private UUID id;
    private Date createdDate;
    private String name;
    private String description;
    private String address;
    private Double maximumHourlyEnergyConsumption;
    private String userName;
}
