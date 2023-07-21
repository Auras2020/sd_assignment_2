package ro.tuc.ds2020.services;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ro.tuc.ds2020.dtos.TimestampEnergyConsumptionDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.TimestampEnergyConsumption;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.TimestampEnergyConsumptionRepository;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@Service
@PropertySource("classpath:device.properties")
public class RabbitMQProducer {

    private static final String COMMA_DELIMITER = ",";
    private final Scanner scanner1;
    private final Scanner scanner2;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Value("${deviceId}")
    private String deviceId;

    private int nr;
    private int minute;
    private double sum;

    private double maxEnergy = 0;

    private final DeviceRepository deviceRepository;
    private final TimestampEnergyConsumptionRepository timestampEnergyConsumptionRepository;

    private final ModelMapper modelMapper;

    public RabbitMQProducer(DeviceRepository deviceRepository, TimestampEnergyConsumptionRepository timestampEnergyConsumptionRepository, ModelMapper modelMapper) throws FileNotFoundException{
        this.deviceRepository = deviceRepository;
        this.timestampEnergyConsumptionRepository = timestampEnergyConsumptionRepository;
        this.modelMapper = modelMapper;
        scanner1 = new Scanner(ResourceUtils.getFile("classpath:sensor.csv"));
        scanner2 = new Scanner(ResourceUtils.getFile("classpath:sensor1.csv"));
        nr = 0;
        minute = -1;
        sum = 0;
    }

    @PostConstruct
    private void getMaxEnergy(){
        Optional<Device> device = this.deviceRepository.findById(UUID.fromString(deviceId));
        device.ifPresent(value -> maxEnergy = value.getMaximumHourlyEnergyConsumption());
    }

    private String getRecordFromLine(String line) {
        String value = "";
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            value = rowScanner.next();
        }
        return value;
    }

    public double getEnergyValueFromCsvFile(Scanner scanner) {
        return scanner.hasNextLine() ? Double.parseDouble(getRecordFromLine(scanner.nextLine())) : -1;
    }

    public String send(Scanner scanner) {
        Date date = new Date(System.currentTimeMillis());
        double value = getEnergyValueFromCsvFile(scanner);
        if(value == -1){
            return "";
        }

        if(date.getMinutes() != minute){
            minute = date.getMinutes();
            double average = nr > 0 ? (sum / nr) : 0;

            if(value > 0){
                TimestampEnergyConsumptionDTO timestampEnergyConsumptionDTO = new TimestampEnergyConsumptionDTO(date, UUID.fromString(deviceId), average);
                rabbitTemplate.convertAndSend(this.queue.getName(), timestampEnergyConsumptionDTO);
                TimestampEnergyConsumption timestampEnergyConsumption = this.modelMapper.map(timestampEnergyConsumptionDTO, TimestampEnergyConsumption.class);
                this.timestampEnergyConsumptionRepository.save(timestampEnergyConsumption);
            }

            nr = 1;
            sum = value;

            if(average > maxEnergy){
                return "Average energy consumption " + average +  " at minute " + date.getMinutes() + " is bigger than device maximum energy consumption!";
            }
        }
        else{
            sum += value;
            nr++;
        }

        return "";
    }

    public String send1(){
        return send(scanner1);
    }

    public String send2(){
        return send(scanner2);
    }

}

