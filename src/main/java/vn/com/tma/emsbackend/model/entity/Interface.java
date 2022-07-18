package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = Constant.INTERFACE_TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "device_id"})})
public class Interface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "port_id")
    private Port port;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private NetworkDevice networkDevice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interface that = (Interface) o;
        return Objects.equals(name, that.name) && state == that.state && dhcp == that.dhcp && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(netmask, that.netmask) && Objects.equals(gateway, that.gateway) && (port == null || Objects.equals(port.getId(), that.port.getId())) && Objects.equals(networkDevice.getId(), that.networkDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state, dhcp, ipAddress, netmask, gateway, port != null ? port.getId() : 0, networkDevice.getId());
    }
}
