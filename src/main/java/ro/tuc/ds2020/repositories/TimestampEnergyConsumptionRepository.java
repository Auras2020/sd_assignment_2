package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.TimestampEnergyConsumption;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimestampEnergyConsumptionRepository extends JpaRepository<TimestampEnergyConsumption, UUID>{

    @Query(value = "SELECT t from TimestampEnergyConsumption t where t.device.id = ?1 order by t.timestamp")
    List<TimestampEnergyConsumption> findAllValues(UUID deviceId);
}
