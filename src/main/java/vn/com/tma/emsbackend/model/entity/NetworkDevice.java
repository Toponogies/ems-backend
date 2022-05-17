package vn.com.tma.emsbackend.model.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = Constant.NETWORK_DEVICE_TABLE)
public class NetworkDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firmware")
    private String firmware;

    @Column(name = "serial")
    private String serial;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "ip_address", unique = true, nullable = false)
    private String ipAddress;

    @Column(name = "label", unique = true, nullable = false)
    private String label;

    @Column(name = "device_type")
    private Enum.NetworkDeviceType deviceType;

    @Column(name = "model")
    private String model;

    @Column(name = "ssh_port", nullable = false)
    private int sshPort;

    @Column(name = "state", nullable = false, columnDefinition = "int default 0")
    private Enum.NetworkDeviceState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id", nullable = false)
    private Credential credential;

    @OneToMany(mappedBy = "networkDevice")
    private List<Port> ports;

    @OneToMany(mappedBy = "networkDevice")
    private List<Interface> interfaces;

}
