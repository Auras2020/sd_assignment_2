package ro.tuc.ds2020.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserAccount;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final DeviceRepository deviceRepository;

    private final UserDeviceService userDeviceService;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, DeviceRepository deviceRepository, UserDeviceService userDeviceService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.userDeviceService = userDeviceService;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> findAll() {
        List<UserAccount> userAccounts = userRepository.findAll();
        return userAccounts.stream().map(userAccount -> this.modelMapper.map(userAccount, UserDTO.class)).collect(Collectors.toList());
    }

    public List<UserDTO> findAllClients() {
        List<UserAccount> userAccounts = userRepository.findAll();
        return userAccounts.stream().map(userAccount -> this.modelMapper.map(userAccount, UserDTO.class)).filter(userDTO -> userDTO.getRole().equals("CLIENT")).collect(Collectors.toList());
    }

    public UserCreateResponseDTO create(UserCreateDTO userCreateDTO){
        UserAccount userAccount = this.modelMapper.map(userCreateDTO, UserAccount.class);
        userAccount.setCreatedDate(new Date(System.currentTimeMillis()));
        UUID deviceId = userRepository.save(userAccount).getId();

        return UserCreateResponseDTO.builder()
                .isOk(true)
                .id(deviceId)
                .message("User added successfully")
                .build();
    }

    public void update(UserAccount dto){
        Optional<UserAccount> userAccountOptional = userRepository.findById(dto.getId());
        UserAccount userAccount = this.modelMapper.map(dto, UserAccount.class);
        userAccount.setId(dto.getId());
        userAccount.setCreatedDate(userAccountOptional.get().getCreatedDate());

        for(Device device: this.deviceRepository.findAll()){
            if(device.getUserName() != null && device.getUserName().equals(userAccountOptional.get().getName())){
                this.userDeviceService.reallocateDeviceFromUser(dto.getId(), device.getId());
                device.setUserName(userAccount.getName());
                this.deviceRepository.save(device);
            }
        }

        if(userAccountOptional.get().getPassword()!=null){
            userAccount.setPassword(userAccountOptional.get().getPassword());
        }
        else {
            userAccount.setPassword("");
        }

        this.userRepository.save(userAccount);
    }

    public void delete(UUID id){
        Optional<UserAccount> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(UserAccount.class.getSimpleName() + " with id: " + id);
        }

        for(Device device: this.deviceRepository.findAll()){
            if(device.getUserName().equals(userOptional.get().getName())){
                this.userDeviceService.deallocateDeviceFromUser(device.getId());
            }
        }

        userRepository.deleteById(id);
    }

    public UserDTO login(String email, String password){
        UserAccount user = null;
        for(UserAccount userAccount: this.userRepository.findAll()){
            if(userAccount.getPassword() != null){
                if(userAccount.getEmail().equals(email) && userAccount.getPassword().equals(password)){
                    user = userAccount;
                    break;
                }
            }
        }
        return user == null ? null : this.modelMapper.map(user, UserDTO.class);
    }

    public void register(String email, String password){
        for(UserAccount userAccount: this.userRepository.findAll()){
            if(userAccount.getEmail().equals(email)){
                userAccount.setPassword(password);
                this.userRepository.save(userAccount);
                break;
            }
        }
    }

}
