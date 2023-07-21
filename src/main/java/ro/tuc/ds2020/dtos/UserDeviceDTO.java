package ro.tuc.ds2020.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDeviceDTO {
    private UUID userId;
    private UUID deviceId;
}
