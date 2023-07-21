package ro.tuc.ds2020.dtos;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private String password;
}
