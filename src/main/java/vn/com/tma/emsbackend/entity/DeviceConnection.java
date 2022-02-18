package vn.com.tma.emsbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_connections")
public class DeviceConnection {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    Credential credential;

    @Column(name = "ip_address", unique = true, nullable = false)
    String ipAddress;

    @Column(name = "port", nullable = false)
    String port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
