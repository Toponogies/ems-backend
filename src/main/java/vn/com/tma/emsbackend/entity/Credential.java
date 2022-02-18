package vn.com.tma.emsbackend.entity;

import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;

import javax.persistence.*;

@Entity
@Table(name = "credentials")
public class Credential {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;
}
