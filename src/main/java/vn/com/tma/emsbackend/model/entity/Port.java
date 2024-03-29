package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = Constant.PORT_TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "device_id"})})
public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector")
    private String connector;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state")
    private Enum.State state;

    @OneToMany(mappedBy = "port", fetch = FetchType.EAGER)
    private List<Interface> interfaces;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private NetworkDevice networkDevice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return Objects.equals(connector, port.connector) && Objects.equals(macAddress, port.macAddress) && Objects.equals(name, port.name) && state == port.state && Objects.equals(networkDevice.getId(), port.networkDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(connector, macAddress, name, state, networkDevice.getId());
    }
}
