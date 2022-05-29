package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.model.entity.NTPServer;

import java.util.List;

@Repository
public interface NTPServerRepository extends JpaRepository<NTPServer, Long> {
    public List<NTPServer> findByNetworkDevice_Id(long deviceId);
}
