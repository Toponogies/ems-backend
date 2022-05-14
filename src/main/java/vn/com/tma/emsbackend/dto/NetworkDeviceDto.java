package vn.com.tma.emsbackend.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.validation.IpAddress;
import vn.com.tma.emsbackend.validation.Port;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NetworkDeviceDto {
    private Long id;

    private String firmware;

    private String serial;

    private String macAddress;

    @IpAddress(message = "Invalid ip address")
    private String ipAddress;

    @NotNull(message = "Label field can not empty")
    private String label;

    private String deviceType;

    private String model;

    @Port(message = "Invalid port number")
    private int port;

    private CredentialDto credentialDto;
}
