package vn.com.tma.emsbackend.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.Enum;

@Getter
@Setter
@Entity
@Table(name = "managed_devices")
public class ManagedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firmware")
    private String firmware;

    @Column(name = "serial", unique = true)
    private String serial;

    @Column(name = "mac_address", unique = true)
    private String macAddress;

    @Column(name = "ip_address", unique = true, nullable = false)
    private String ipAddress;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "device_type")
    private Enum.ManagedDeviceType deviceType;

    @Column(name = "model")
    private String model;

    @Column(name = "ssh_port", nullable = false)
    private long SSHPort;

    @Column(name = "state", nullable = false,columnDefinition = "int default 0")
    private Enum.ManagedDeviceState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private Credential credential;
}
