package vn.com.tma.emsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.model.entity.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    boolean existsByName(String name);

    Credential findByName(String name);
}
