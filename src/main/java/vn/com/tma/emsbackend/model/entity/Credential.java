package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = Constant.CREDENTIAL_TABLE)
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "credential", fetch = FetchType.EAGER)
    List<NetworkDevice> devices = new ArrayList<>();
}
