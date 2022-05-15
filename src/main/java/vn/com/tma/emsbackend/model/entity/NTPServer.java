package vn.com.tma.emsbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.*;

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
}
