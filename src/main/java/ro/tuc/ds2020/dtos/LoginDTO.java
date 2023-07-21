package ro.tuc.ds2020.dtos;

import lombok.Data;

@Data
public class LoginDTO {

    private String id;
    private String email;
    private String password;
    private String role;
}
