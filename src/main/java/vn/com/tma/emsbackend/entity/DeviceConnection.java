package vn.com.tma.emsbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_connections")
public class DeviceConnection {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    Credential credential;

    @Column(name = "ip_address", unique = true, nullable = false)
    String ipAddress;

    @Column(name = "port", nullable = false)
    String port;
}
