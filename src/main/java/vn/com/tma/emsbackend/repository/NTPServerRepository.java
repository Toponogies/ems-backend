package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.entity.NTPServer;

@Repository
public interface NTPServerRepository extends JpaRepository<NTPServer, Long> {
}
