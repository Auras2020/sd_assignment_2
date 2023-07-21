package ro.tuc.ds2020.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateResponseDTO {

    UUID id;
    boolean isOk;
    String message;
}
