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

    @IpAddress(message = "Invalid ip address")
    private String ipAddress;

    @NotNull(message = "Label field can not empty")
    private String label;

    private String deviceType;

    private String model;

    @Port(message = "Invalid port number")
    private int sshPort;

    private String state;

    private Long credentialId;
}
