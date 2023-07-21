package ro.tuc.ds2020.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceCreateDTO {
    private String name;
    private String description;
    private String address;
    private Double maximumHourlyEnergyConsumption;
    private String userName;
}
