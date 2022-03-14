package vn.com.tma.emsbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagedDeviceDto {
    private long id;

    private String firmware;

    private String serial;

    private String macAddress;

    private String ipAddress;

    private String label;

    private String deviceType;

    private String model;

    private long port;

    private long credentialId;

    private String credentialName;

    private String credentialUsername;
}
