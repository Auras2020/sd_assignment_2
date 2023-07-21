package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.UserDeviceDTO;
import ro.tuc.ds2020.services.UserDeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user-device")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    @Autowired
    public UserDeviceController(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    @PostMapping("/allocate")
    public void allocateDeviceToUser(@RequestBody UserDeviceDTO userDeviceDTO){
        this.userDeviceService.allocateDeviceToUser(userDeviceDTO.getUserId(), userDeviceDTO.getDeviceId());
    }

    @PostMapping("/deallocate")
    public void deallocateDeviceFromUser(@RequestBody UserDeviceDTO userDeviceDTO){
        this.userDeviceService.deallocateDeviceFromUser(userDeviceDTO.getDeviceId());
    }

    @PostMapping("/reallocate")
    public void reallocateDeviceFromUser(@RequestBody UserDeviceDTO userDeviceDTO){
        this.userDeviceService.reallocateDeviceFromUser(userDeviceDTO.getUserId(), userDeviceDTO.getDeviceId());
    }

    @GetMapping("findDevices/{userId}")
    public List<DeviceDTO> findDevicesAllocatedToAnUser(@PathVariable("userId") UUID userId){
        return this.userDeviceService.findDevicesAllocatedToAnUser(userId);
    }
}
