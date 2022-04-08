package vn.com.tma.emsbackend.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.validation.IpAddress;
import vn.com.tma.emsbackend.validation.Port;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ManagedDeviceRequestDto {
    @NotNull(message = "Label field can not empty")
    private String label;

    @IpAddress(message = "Invalid ip address")
    private String ipAddress;

    @Port(message = "Invalid port number")
    private long port;

    @NotNull(message = "Credential can not empty")
    private long credentialId;
}
