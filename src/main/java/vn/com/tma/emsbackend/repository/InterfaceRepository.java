package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.model.entity.Interface;

import java.util.List;

@Repository
public interface InterfaceRepository extends JpaRepository<Interface, Long> {
    List<Interface> findByNetworkDevice_Id(Long networkDeviceId);

    Interface findByPort_Id(Long portId);

    Interface findByName(String name);

    boolean existsByName(String name);

    void deleteByPortId(Long portId);

    List<Interface> findByNetworkDeviceId(long deviceId);
}
