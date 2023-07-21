package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampEnergyConsumptionDTO {

    private Date timestamp;
    private UUID deviceId;
    private Double energyConsumption;
}
