package vn.com.tma.emsbackend.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Enum;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = Constant.INTERFACE_TABLE)
public class Interface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "state", nullable = false)
    private Enum.State state;

    @Column(name = "dhcp", nullable = false)
    private Enum.State dhcp;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "netmask")
    private String netmask;

    @Column(name = "gateway")
    private String gateway;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Port port;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_device_id")
    private NetworkDevice networkDevice;

}
