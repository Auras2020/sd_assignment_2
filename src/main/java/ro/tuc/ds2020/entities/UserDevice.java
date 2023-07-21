package ro.tuc.ds2020.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "userAccount")
public class UserDevice {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "pg-uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name="device_id", nullable=false)
    private Device device;

    public UserDevice(UserAccount userAccount, Device device) {
        this.userAccount = userAccount;
        this.device = device;
    }
}
