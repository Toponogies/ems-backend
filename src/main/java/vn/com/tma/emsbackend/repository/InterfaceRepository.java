package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.entity.Interface;

@Repository
public interface InterfaceRepository extends JpaRepository<Interface, Long> {
}