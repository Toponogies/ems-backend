package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.validation.IpAddress;
import vn.com.tma.emsbackend.model.validation.Port;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NetworkDeviceDTO {
    private Long id;

    private String firmware;

    private String serial;

    private String macAddress;

    @NotEmpty(message = "IP address field can not empty")
    @IpAddress(message = "Invalid IP address")
    private String ipAddress;

    @NotEmpty(message = "Label field can not empty")
    private String label;

    private String deviceType;

    private String model;

    @NotNull(message = "Port number field can not be null")
    @Port(message = "Invalid port number")
    private int sshPort;

    private String state;

    @NotNull(message = "Device must have a credential")
    private String credential;

    private String resyncStatus;
}
