package ro.tuc.ds2020.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Device {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maximum_hourly_energy_consumption", nullable = false)
    private Double maximumHourlyEnergyConsumption;

    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy="device", cascade = CascadeType.ALL)
    private Set<UserDevice> userDevices;

    @OneToMany(mappedBy="device", cascade = CascadeType.ALL)
    private Set<TimestampEnergyConsumption> timestampEnergyConsumption;
}
