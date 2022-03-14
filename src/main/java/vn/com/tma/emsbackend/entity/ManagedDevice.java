package vn.com.tma.emsbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "managed_device")
public class ManagedDevice {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "firmware")
    private String firmware;

    @Column(name = "serial", unique = true)
    private String serial;

    @Column(name = "mac_address", unique = true)
    private String macAddress;

    @Column(name = "ip_address", unique = true, nullable = false)
    private String ipAddress;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "model")
    private String model;

    @Column(name = "port", nullable = false)
    private long port;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private Credential credential;
}
