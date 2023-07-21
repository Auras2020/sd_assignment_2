package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.LoginDTO;
import ro.tuc.ds2020.dtos.UserCreateDTO;
import ro.tuc.ds2020.dtos.UserCreateResponseDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.UserAccount;
import ro.tuc.ds2020.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDTO> getAll(){
        return this.userService.findAll();
    }

    @GetMapping("/clients")
    public List<UserDTO> getAllClients(){
        return this.userService.findAllClients();
    }

    @PutMapping()
    public UserCreateResponseDTO create(@RequestBody UserCreateDTO userCreateDTO){
        return this.userService.create(userCreateDTO);
    }

    @PostMapping()
    public void update(@RequestBody UserAccount userDTO){
        this.userService.update(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id){
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO loginDTO){
        return this.userService.login(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @PostMapping("/register")
    public void register(@RequestBody LoginDTO loginDTO){
        this.userService.register(loginDTO.getEmail(), loginDTO.getPassword());
    }
}
