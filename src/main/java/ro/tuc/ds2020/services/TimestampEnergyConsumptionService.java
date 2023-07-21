package ro.tuc.ds2020.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.ChartDTO;
import ro.tuc.ds2020.dtos.TimestampEnergyConsumptionDTO;
import ro.tuc.ds2020.entities.TimestampEnergyConsumption;
import ro.tuc.ds2020.repositories.TimestampEnergyConsumptionRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimestampEnergyConsumptionService {

    private final TimestampEnergyConsumptionRepository timestampEnergyConsumptionRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TimestampEnergyConsumptionService(TimestampEnergyConsumptionRepository timestampEnergyConsumptionRepository, ModelMapper modelMapper) {
        this.timestampEnergyConsumptionRepository = timestampEnergyConsumptionRepository;
        this.modelMapper = modelMapper;
    }

    private List<TimestampEnergyConsumptionDTO> findAllValues(UUID deviceId, Date date){
        List<TimestampEnergyConsumption> timestampEnergyConsumptionList = timestampEnergyConsumptionRepository.findAllValues(deviceId);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        return timestampEnergyConsumptionList.stream().map(timestampEnergyConsumption ->
                this.modelMapper.map(timestampEnergyConsumption, TimestampEnergyConsumptionDTO.class))
                .filter(timestampEnergyConsumptionDTO -> (dateFormatter.format(timestampEnergyConsumptionDTO.getTimestamp())).equals(dateFormatter.format(date)))
                .collect(Collectors.toList());
    }

    public List<ChartDTO> findChartData(UUID deviceId, Date date){
        List<TimestampEnergyConsumptionDTO> timestampEnergyConsumptionDTOList = findAllValues(deviceId, date);
        List<ChartDTO> chartDTOList = new ArrayList<>();
        for(TimestampEnergyConsumptionDTO timestampEnergyConsumptionDTO: timestampEnergyConsumptionDTOList){
            ChartDTO chartDTO = new ChartDTO(timestampEnergyConsumptionDTO.getTimestamp().getMinutes(), timestampEnergyConsumptionDTO.getEnergyConsumption());
            chartDTOList.add(chartDTO);
        }
        return chartDTOList;
    }

}
