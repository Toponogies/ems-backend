package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.tma.emsbackend.entity.ManagedDevice;

public interface ManagedDeviceRepository extends JpaRepository<ManagedDevice, Long> {
}
