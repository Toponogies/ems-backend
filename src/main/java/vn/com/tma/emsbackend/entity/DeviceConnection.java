package vn.com.tma.emsbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "device_connections")
public class DeviceConnection {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    Credential credential;

    @Column(name = "ip_address", unique = true, nullable = false)
    String ipAddress;

    @Column(name = "port", nullable = false)
    String port;
}
