package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tma.emsbackend.entity.Port;

import java.util.List;

public interface PortRepository  extends JpaRepository<Port, Long> {
    List<Port> findByNetworkDeviceId(Long networkDeviceId);
}
