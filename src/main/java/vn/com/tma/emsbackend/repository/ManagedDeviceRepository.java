package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tma.emsbackend.entity.ManagedDevice;

import java.util.List;

public interface ManagedDeviceRepository extends JpaRepository<ManagedDevice, Long> {
    @Query("select md from ManagedDevice md where md.ipAddress=:ip_address")
    ManagedDevice getFirstByIpAddress(@Param("ip_address") String ipAddress);
}
