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
@Table(name = Constant.NTP_SERVER_TABLE)
public class NTPServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_address")
    private String serverAddress;

    @Column(name = "state")
    private Enum.State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_device_id")
    private NetworkDevice networkDevice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NTPServer ntpServer = (NTPServer) o;
        return Objects.equals(serverAddress, ntpServer.serverAddress) && state == ntpServer.state && Objects.equals(networkDevice.getId(), ntpServer.networkDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverAddress, state, networkDevice.getId());
    }
}
