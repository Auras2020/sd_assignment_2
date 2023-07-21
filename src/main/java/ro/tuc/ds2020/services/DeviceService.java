package ro.tuc.ds2020.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.dtos.DeviceCreateDTO;
import ro.tuc.ds2020.dtos.DeviceCreateResponseDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final UserDeviceService userDeviceService;

    private final ModelMapper modelMapper;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository, UserDeviceService userDeviceService, ModelMapper modelMapper) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.userDeviceService = userDeviceService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<DeviceDTO> findAll(){
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(device -> this.modelMapper.map(device, DeviceDTO.class)).collect(Collectors.toList());
    }

    public DeviceCreateResponseDTO create(DeviceCreateDTO deviceCreateDTO){
        Device device = this.modelMapper.map(deviceCreateDTO, Device.class);

        device.setCreatedDate(new Date(System.currentTimeMillis()));
        UUID deviceId = deviceRepository.save(device).getId();
        return DeviceCreateResponseDTO.builder()
                .isOk(true)
                .id(deviceId)
                .message("Device added successfully")
                .build();
    }

    public void update(DeviceDTO deviceDTO){
        Optional<Device> deviceOptional = deviceRepository.findById(deviceDTO.getId());
        Device device = this.modelMapper.map(deviceDTO, Device.class);
        device.setId(deviceDTO.getId());
        device.setCreatedDate(deviceOptional.get().getCreatedDate());

        if(deviceOptional.get().getUserName()!=null){
            device.setUserName(deviceOptional.get().getUserName());
        }
        else {
            device.setUserName("");
        }

        this.deviceRepository.save(device);
    }

    public void deleteById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            throw new EntityNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.deleteById(id);
    }

    public DeviceDTO findById(UUID id){
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        return deviceOptional.map(device -> this.modelMapper.map(device, DeviceDTO.class)).orElse(null);
    }

}
