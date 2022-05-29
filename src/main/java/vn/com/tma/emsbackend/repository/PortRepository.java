package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.model.entity.Port;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortRepository extends JpaRepository<Port, Long> {
    List<Port> findByNetworkDeviceId(Long networkDeviceId);

    Optional<Port> findByIdAndNetworkDeviceId(Long id, Long deviceId);

}
