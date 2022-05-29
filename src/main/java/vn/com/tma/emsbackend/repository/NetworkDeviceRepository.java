package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;

import java.util.List;

@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, Long> {
    NetworkDevice findByIpAddress(String ipAddress);

    List<NetworkDevice> findAllByDeviceType(Enum.NetworkDeviceType deviceType);

    boolean existsByLabel(String label);

    NetworkDevice findByLabel(String label);

    boolean existsByIpAddress(String ipAddress);
}
