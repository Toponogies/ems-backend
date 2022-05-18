package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = Constant.INTERFACE_TABLE)
public class Interface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    private Enum.State state;

    @Column(name = "dhcp", nullable = false)
    private Enum.State dhcp;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "netmask")
    private String netmask;

    @Column(name = "gateway")
    private String gateway;

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "port_id", unique = true)
    private Port port;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_device_id")
    private NetworkDevice networkDevice;
}
