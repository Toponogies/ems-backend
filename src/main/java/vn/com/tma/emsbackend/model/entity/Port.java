package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = Constant.PORT_TABLE)
public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector")
    private String connector;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private Enum.State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_device_id")
    private NetworkDevice networkDevice;
}
