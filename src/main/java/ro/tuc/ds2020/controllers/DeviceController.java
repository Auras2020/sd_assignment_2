package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceCreateDTO;
import ro.tuc.ds2020.dtos.DeviceCreateResponseDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public List<DeviceDTO> getAll(){
        return this.deviceService.findAll();
    }

    @PutMapping()
    public DeviceCreateResponseDTO create(@RequestBody DeviceCreateDTO deviceCreateDTO){
        return this.deviceService.create(deviceCreateDTO);
    }

    @PostMapping()
    public void update(@RequestBody DeviceDTO deviceDTO){
        this.deviceService.update(deviceDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id){
        this.deviceService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/findDevice/{id}")
    public DeviceDTO findById(@PathVariable("id") UUID id){
        return this.deviceService.findById(id);
    }
}
