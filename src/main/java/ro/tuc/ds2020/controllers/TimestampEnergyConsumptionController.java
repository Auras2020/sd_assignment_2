package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ChartDTO;
import ro.tuc.ds2020.dtos.DateDeviceIdDTO;
import ro.tuc.ds2020.services.TimestampEnergyConsumptionService;

import java.util.List;

@RestController
@RequestMapping(value = "/timestamp-energy-consumption")
public class TimestampEnergyConsumptionController {

    private final TimestampEnergyConsumptionService timestampEnergyConsumptionService;

    @Autowired
    public TimestampEnergyConsumptionController(TimestampEnergyConsumptionService timestampEnergyConsumptionService) {
        this.timestampEnergyConsumptionService = timestampEnergyConsumptionService;
    }

    @PostMapping("/findChart")
    public List<ChartDTO> findChartData(@RequestBody DateDeviceIdDTO dateDeviceIdDTO){
        return this.timestampEnergyConsumptionService.findChartData(dateDeviceIdDTO.getDeviceId(), dateDeviceIdDTO.getTimestamp());
    }
}
