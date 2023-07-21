package ro.tuc.ds2020.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserAccount;
import ro.tuc.ds2020.entities.UserDevice;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserDeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    private final UserRepository userRepository;

    private final DeviceRepository deviceRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserDeviceService(UserDeviceRepository userDeviceRepository, UserRepository userRepository, DeviceRepository deviceRepository, ModelMapper modelMapper) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
    }

    public boolean checkIfDeviceIsAllocated(UUID deviceId) {
        UserDevice userDevice = this.userDeviceRepository.findAllocatedDevice(deviceId);
        return userDevice != null;
    }

    public void allocateDeviceToUser(UUID userId, UUID deviceId){
        if(!checkIfDeviceIsAllocated(deviceId)){
            Optional<Device> device = this.deviceRepository.findById(deviceId);
            Optional<UserAccount> userAccount = this.userRepository.findById(userId);
            if(userAccount.isPresent() && device.isPresent()){
                UserDevice userDevice= new UserDevice(userAccount.get(), device.get());
                device.get().setUserName(userAccount.get().getName());
                deviceRepository.save(device.get());
                this.userDeviceRepository.save(userDevice);
            }
        }
    }

    public void deallocateDeviceFromUser(UUID deviceId){
        if(checkIfDeviceIsAllocated(deviceId)){
            UserDevice userDevice = this.userDeviceRepository.findAllocatedDevice(deviceId);
            Optional<Device> device = this.deviceRepository.findById(deviceId);
            if(device.isPresent()) {
                device.get().setUserName("");
                this.deviceRepository.save(device.get());
                this.userDeviceRepository.delete(userDevice);
            }
        }
    }

    public void reallocateDeviceFromUser(UUID userId, UUID deviceId) {
        if (checkIfDeviceIsAllocated(deviceId)) {
            UserDevice userDevice = this.userDeviceRepository.findAllocatedDevice(deviceId);
            if(!userDevice.getUserAccount().getId().equals(userId)) {
                Optional<Device> device = this.deviceRepository.findById(deviceId);
                Optional<UserAccount> userAccount = this.userRepository.findById(userId);
                if(userAccount.isPresent() && device.isPresent()){
                    UserDevice newUserDevice = new UserDevice(userAccount.get(), device.get());
                    device.get().setUserName(userAccount.get().getName());
                    this.deviceRepository.save(device.get());
                    this.userDeviceRepository.delete(userDevice);
                    this.userDeviceRepository.save(newUserDevice);
                }
            }
        }
    }

    public List<DeviceDTO> findDevicesAllocatedToAnUser(UUID userId) {
        List<UserDevice> userDevices = this.userDeviceRepository.findDevicesAllocatedToAnUser(userId);
        return userDevices.stream().map(userDevice -> this.modelMapper.map(userDevice.getDevice(), DeviceDTO.class)).collect(Collectors.toList());
    }
}
