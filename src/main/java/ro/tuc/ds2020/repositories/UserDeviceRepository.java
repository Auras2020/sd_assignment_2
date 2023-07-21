package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.UserDevice;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

    @Query(value = "SELECT u FROM UserDevice u where u.device.id = ?1")
    UserDevice findAllocatedDevice(UUID deviceId);

    @Query("SELECT ud FROM UserDevice ud where ud.userAccount.id = ?1")
    List<UserDevice> findDevicesAllocatedToAnUser(UUID userId);
}
