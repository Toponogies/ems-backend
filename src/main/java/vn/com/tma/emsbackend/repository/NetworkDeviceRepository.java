package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.entity.NetworkDevice;

import java.util.List;

@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, Long> {
    NetworkDevice findByIpAddress(String ipAddress);

    List<NetworkDevice> findByDeviceType(Enum.NetworkDeviceType deviceType);
}
