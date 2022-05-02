package vn.com.tma.emsbackend.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Enum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = Constant.NTP_SERVER_TABLE)
public class NTPServer {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_address")
    private String serverAddress;

    @Column(name = "state")
    private Enum.State state;
}
