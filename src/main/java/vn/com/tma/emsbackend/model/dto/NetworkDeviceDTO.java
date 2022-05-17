package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.validation.IpAddress;
import vn.com.tma.emsbackend.model.validation.Port;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NetworkDeviceDTO {
    private Long id;

    private String firmware;

    private String serial;

    private String macAddress;

    @IpAddress(message = "Invalid IP address")
    @NotNull(message = "IP address field can not empty")
    private String ipAddress;

    @NotNull(message = "Label field can not empty")
    private String label;

    private String deviceType;

    private String model;

    @Port(message = "Invalid port number")
    @NotNull(message = "Port number field can not be empty")
    private int sshPort;

    @NotNull(message = "State can not be empty")
    private String state;

    @NotNull(message = "Device must have a credential")
    private Long credentialId;
}
