package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = Constant.CREDENTIAL_TABLE)
public class Credential {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
}
