package ro.tuc.ds2020.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    private String name;
    private String email;
    private String role;
    private String password;
}
